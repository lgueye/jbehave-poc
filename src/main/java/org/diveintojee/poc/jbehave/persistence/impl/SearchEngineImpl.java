/*
 *
 */
package org.diveintojee.poc.jbehave.persistence.impl;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.diveintojee.poc.jbehave.business.impl.SearchResponseToSearchResultConverter;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.FieldOperatorValueTriple;
import org.diveintojee.poc.jbehave.domain.OrderBy;
import org.diveintojee.poc.jbehave.domain.Persistable;
import org.diveintojee.poc.jbehave.domain.SearchQuery;
import org.diveintojee.poc.jbehave.domain.SearchResult;
import org.diveintojee.poc.jbehave.domain.SortDirection;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.diveintojee.poc.jbehave.persistence.AdvertToJsonByteArrayConverter;
import org.diveintojee.poc.jbehave.persistence.SearchEngine;
import org.diveintojee.poc.jbehave.persistence.SearchOperator;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class SearchEngineImpl implements SearchEngine {

	@Autowired
	private Client									elasticSearchClient;

	@Autowired
	@Qualifier(AdvertToJsonByteArrayConverter.BEAN_ID)
	private AdvertToJsonByteArrayConverter			advertToJsonByteArrayConverter;

	@Autowired
	@Qualifier(SearchResponseToSearchResultConverter.BEAN_ID)
	private SearchResponseToSearchResultConverter	searchResponseToSearchResultConverter;

	/**
	 * @param searchRequestBuilder
	 */
	void applyDefaultSort(final SearchRequestBuilder searchRequestBuilder) {

		searchRequestBuilder.addSort(OrderBy.DEFAULT_FIELD, SortOrder.DESC);

	}

	/**
	 * @param query
	 * @return
	 */
	SearchRequestBuilder convertSearchQueryToRequestBuilder(final SearchQuery query) {

		final SearchRequestBuilder searchRequestBuilder = this.elasticSearchClient.prepareSearch(
				Utils.pluralize(Advert.class))//
				.setTypes(Utils.minimize(Advert.class));

		if (query == null) return searchRequestBuilder;

		searchRequestBuilder.setFrom(query.getStartPage() - 1);

		searchRequestBuilder.setSize(query.getItemsPerPage());

		final List<String> clauses = extractSearchClauses(query.getQueryString());

		QueryBuilder queryBuilder = resolveQueryBuilder(clauses);

		if (searchRequestBuilder != null) {

			searchRequestBuilder.setQuery(queryBuilder);

		}

		applySort(searchRequestBuilder, query.getOrderByList());

		return searchRequestBuilder;

	}

	QueryBuilder resolveQueryBuilder(List<String> clauses) {

		QueryBuilder queryBuilder = null;

		if (CollectionUtils.isEmpty(clauses)) return QueryBuilders.matchAllQuery();

		if (CollectionUtils.size(clauses) == 1) {

			String clause = clauses.get(0);

			final FieldOperatorValueTriple fieldOperatorValueTriple = FieldOperatorValueTriple.fromClause(clause);

			if (fieldOperatorValueTriple.isValueOnly()) return QueryBuilders.queryString(
					fieldOperatorValueTriple.getValue()).defaultField("_all");

		}

		queryBuilder = boolQuery();

		for ( final String clause : clauses ) {

			final FieldOperatorValueTriple fieldOperatorValueTriple = FieldOperatorValueTriple.fromClause(clause);

			switch (fieldOperatorValueTriple.getOperator()) {
				case EXACT_MATCH_OPERATOR:
					((BoolQueryBuilder) queryBuilder).must(termQuery(fieldOperatorValueTriple.getField(),
							fieldOperatorValueTriple.getValue()));
					break;
				case FULL_TEXT_OPERATOR:
					((BoolQueryBuilder) queryBuilder).must(queryString(fieldOperatorValueTriple.getValue())
							.defaultField(fieldOperatorValueTriple.getField()));
					break;
				default:
					throw new UnsupportedOperationException("Unsupported search operator = "
							+ fieldOperatorValueTriple.getOperator());
			}

		}

		return queryBuilder;

	}

	void applySort(final SearchRequestBuilder searchRequestBuilder, final Set<OrderBy> orderByList) {

		if (CollectionUtils.isNotEmpty(orderByList)) {

			for ( final OrderBy orderBy : orderByList ) {

				searchRequestBuilder.addSort(orderBy.getField(),
						orderBy.getSortDirection() == SortDirection.ASC ? SortOrder.ASC : SortOrder.DESC);

			}

		}

		if (orderSpecificationDoesntContainDefaultSortField(orderByList)) {

			applyDefaultSort(searchRequestBuilder);

		}

	}

	/**
	 * @param queryString
	 * @return
	 */
	List<String> extractSearchClauses(final String queryString) {

		if (StringUtils.isEmpty(queryString)) return null;

		List<String> clauses = Arrays.asList(queryString.split(CLAUSES_SEPARATOR));

		Collection<String> cleanClauses = Collections2.filter(clauses, new Predicate<String>() {

			/**
			 * @see com.google.common.base.Predicate#apply(java.lang.Object)
			 */
			@Override
			public boolean apply(String input) {
				return StringUtils.isNotEmpty(input)//
						&& !input.trim().equals(SearchOperator.EXACT_MATCH_OPERATOR.toString())//
						&& !input.trim().equals(SearchOperator.FULL_TEXT_OPERATOR.toString()) //
						&& !input.trim().endsWith(SearchOperator.EXACT_MATCH_OPERATOR.toString())//
						&& !input.trim().endsWith(SearchOperator.FULL_TEXT_OPERATOR.toString());
			}

		});

		return new ArrayList<String>(cleanClauses);

	}

	/**
	 * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#findById(java.lang.String,
	 *      java.lang.Class, java.lang.Long)
	 */
	@Override
	public SearchResponse findById(final Class<?> type, final Long id) {

		Preconditions.checkArgument(type != null, "type is required");

		Preconditions.checkArgument(id != null, "id is required");

		final TermQueryBuilder idClause = QueryBuilders.termQuery("_id", id);

		final QueryBuilder builder = QueryBuilders.boolQuery().must(idClause);

		return this.elasticSearchClient.prepareSearch(Utils.pluralize(type)).setTypes(Utils.minimize(type))
				.setQuery(builder).execute().actionGet();
	}

	/**
	 * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#getClient()
	 */
	@Override
	public Client getClient() {
		return this.elasticSearchClient;
	}

	/**
	 * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#index(java.lang.String,
	 *      java.lang.Class, java.lang.Object)
	 */
	@Override
	public void index(final Class<?> type, final Object document) {

		Preconditions.checkArgument(type != null, "type is required");

		Preconditions.checkArgument(document != null, "document is required");

		Preconditions.checkArgument(document instanceof Advert, "advert document is required");

		Preconditions.checkArgument(((Persistable) document).getId() != null, "document id is required");

		Advert advert = (Advert) document;

		byte[] source = this.advertToJsonByteArrayConverter.convert(advert);

		this.elasticSearchClient
				.prepareIndex(Utils.pluralize(type), Utils.minimize(type), ((Persistable) document).getId().toString())//
				.setRefresh(true) //
				.setSource(source) //
				.execute().actionGet();
	}

	/**
	 * @param orderByList
	 * @return
	 */
	boolean orderSpecificationDoesntContainDefaultSortField(final Set<OrderBy> orderByList) {

		if (CollectionUtils.isEmpty(orderByList)) return true;

		Collection<String> orderFields = Collections2.transform(orderByList, new Function<OrderBy, String>() {

			@Override
			public String apply(final OrderBy input) {

				return input.getField();

			}
		});

		orderFields = Collections2.filter(orderFields, new Predicate<String>() {
			/**
			 * @see com.google.common.base.Predicate#apply(java.lang.Object)
			 */
			@Override
			public boolean apply(String input) {
				return StringUtils.isNotEmpty(input);
			}

		});

		return CollectionUtils.isEmpty(orderFields) || !orderFields.contains(OrderBy.DEFAULT_FIELD);

	}

	/**
	 * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#removeFromIndex(java.lang.String,
	 *      java.lang.Class, java.lang.Long)
	 */
	@Override
	public void removeFromIndex(final Class<?> type, final Long id) {

		Preconditions.checkArgument(type != null, "type is required");

		Preconditions.checkArgument(id != null, "Id is required");

		this.elasticSearchClient.prepareDelete(Utils.pluralize(type), Utils.minimize(type), id.toString())
				.setRefresh(true).execute().actionGet();

	}

	/**
	 * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#search(org.diveintojee.poc.jbehave.domain.SearchQuery)
	 */

	@Override
	public SearchResult search(final SearchQuery searchQuery) {

		final SearchRequestBuilder searchRequestBuilder = convertSearchQueryToRequestBuilder(searchQuery);

		final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		SearchResult result = this.searchResponseToSearchResultConverter.convert(searchResponse);

		return result;

	}

}
