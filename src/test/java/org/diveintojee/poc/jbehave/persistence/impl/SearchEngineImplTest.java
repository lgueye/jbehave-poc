/*
 *
 */
package org.diveintojee.poc.jbehave.persistence.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.diveintojee.poc.jbehave.domain.Address;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.OrderBy;
import org.diveintojee.poc.jbehave.domain.Persistable;
import org.diveintojee.poc.jbehave.domain.SearchQuery;
import org.diveintojee.poc.jbehave.domain.SortDirection;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.diveintojee.poc.jbehave.persistence.AdvertToJsonByteArrayConverter;
import org.diveintojee.poc.jbehave.persistence.SearchEngine;
import org.diveintojee.poc.jbehave.persistence.SearchOperator;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.action.delete.DeleteRequestBuilder;
import org.elasticsearch.client.action.index.IndexRequestBuilder;
import org.elasticsearch.client.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchEngineImplTest {

	@Mock
	private Client							elasticSearchClient;

	@Mock
	private AdvertToJsonByteArrayConverter	advertToJsonByteArrayConverter;

	@InjectMocks
	private final SearchEngine				underTest	= new SearchEngineImpl();

	@Test(expected = IllegalArgumentException.class)
	public void findByIdWillThrowIllegalArgumentExceptionWithNullId() {
		this.underTest.findById(Advert.class, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void findByIdWillThrowIllegalArgumentExceptionWithNullType() {
		this.underTest.findById(null, 6L);
	}

	@Test
	public void indexWillInvokeElasticSearch() {
		// Given
		final Class<?> type = Advert.class;
		final Long id = 5L;
		final byte[] documentAsBytes = "".getBytes();
		final Advert document = new Advert();
		document.setId(id);
		final IndexRequestBuilder indexRequestBuilder = Mockito.mock(IndexRequestBuilder.class);
		@SuppressWarnings("unchecked")
		final ListenableActionFuture<IndexResponse> listenableActionFuture = Mockito.mock(ListenableActionFuture.class);
		Mockito.when(
				this.elasticSearchClient.prepareIndex(Utils.pluralize(type), Utils.minimize(type),
						((Persistable) document).getId().toString())).thenReturn(indexRequestBuilder);
		Mockito.when(indexRequestBuilder.setRefresh(true)).thenReturn(indexRequestBuilder);
		Mockito.when(this.advertToJsonByteArrayConverter.convert(document)).thenReturn(documentAsBytes);
		Mockito.when(indexRequestBuilder.setSource(documentAsBytes)).thenReturn(indexRequestBuilder);
		Mockito.when(indexRequestBuilder.execute()).thenReturn(listenableActionFuture);

		// When
		this.underTest.index(type, document);

		// Then
		Mockito.verify(this.elasticSearchClient).prepareIndex(Utils.pluralize(type), Utils.minimize(type),
				((Persistable) document).getId().toString());
		Mockito.verify(indexRequestBuilder).setRefresh(true);
		Mockito.verify(this.advertToJsonByteArrayConverter).convert(document);
		Mockito.verify(indexRequestBuilder).setSource(documentAsBytes);
		Mockito.verify(indexRequestBuilder).execute();
		Mockito.verify(listenableActionFuture).actionGet();

		Mockito.verifyNoMoreInteractions(this.elasticSearchClient, indexRequestBuilder,
				this.advertToJsonByteArrayConverter, listenableActionFuture);
	}

	@Test(expected = IllegalArgumentException.class)
	public void indexWillThrowIllegalArgumentExceptionIfDocumentIsNotAnAdvert() {
		this.underTest.index(Address.class, new Address());
	}

	@Test(expected = IllegalArgumentException.class)
	public void indexWillThrowIllegalArgumentExceptionWithNullDocument() {
		this.underTest.index(Advert.class, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void indexWillThrowIllegalArgumentExceptionWithNullDocumentId() {
		this.underTest.index(Advert.class, new Advert());
	}

	@Test(expected = IllegalArgumentException.class)
	public void indexWillThrowIllegalArgumentExceptionWithNullType() {
		this.underTest.index(null, new Advert());
	}

	@Test
	public void removeFromIndexWillInvokeElasticSearch() {
		// Given
		final Class<?> type = Advert.class;
		final Long id = 5L;
		final DeleteRequestBuilder deleteRequestBuilder = Mockito.mock(DeleteRequestBuilder.class);
		@SuppressWarnings("unchecked")
		final ListenableActionFuture<DeleteResponse> listenableActionFuture = Mockito
				.mock(ListenableActionFuture.class);
		Mockito.when(this.elasticSearchClient.prepareDelete(Utils.pluralize(type), Utils.minimize(type), id.toString()))
				.thenReturn(deleteRequestBuilder);
		Mockito.when(deleteRequestBuilder.setRefresh(true)).thenReturn(deleteRequestBuilder);
		Mockito.when(deleteRequestBuilder.execute()).thenReturn(listenableActionFuture);

		// When
		this.underTest.removeFromIndex(type, id);

		// Then
		Mockito.verify(this.elasticSearchClient).prepareDelete(Utils.pluralize(type), Utils.minimize(type),
				id.toString());
		Mockito.verify(deleteRequestBuilder).setRefresh(true);
		Mockito.verify(deleteRequestBuilder).execute();
		Mockito.verify(listenableActionFuture).actionGet();
		Mockito.verifyNoMoreInteractions(this.elasticSearchClient, deleteRequestBuilder, listenableActionFuture);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeFromIndexWillThrowIllegalArgumentExceptionWithNullId() {
		this.underTest.removeFromIndex(Advert.class, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeFromIndexWillThrowIllegalArgumentExceptionWithNullType() {
		this.underTest.removeFromIndex(null, 6L);
	}

	@Test
	public void searchWillInvokeElasticSearch() {
		// Given
		final Class<?> type = Advert.class;
		final Long id = 5L;
		final SearchRequestBuilder searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);
		@SuppressWarnings("unchecked")
		final ListenableActionFuture<SearchResponse> listenableActionFuture = Mockito
				.mock(ListenableActionFuture.class);
		Mockito.when(this.elasticSearchClient.prepareSearch(Utils.pluralize(type))).thenReturn(searchRequestBuilder);
		Mockito.when(searchRequestBuilder.setTypes(Utils.minimize(type))).thenReturn(searchRequestBuilder);
		Mockito.when(searchRequestBuilder.setQuery(Matchers.any(QueryBuilder.class))).thenReturn(searchRequestBuilder);
		Mockito.when(searchRequestBuilder.execute()).thenReturn(listenableActionFuture);

		// When
		this.underTest.findById(type, id);
		// Then
		Mockito.verify(this.elasticSearchClient).prepareSearch(Utils.pluralize(type));
		Mockito.verify(searchRequestBuilder).setTypes(Utils.minimize(type));
		Mockito.verify(searchRequestBuilder).setQuery(Matchers.any(QueryBuilder.class));
		Mockito.verify(searchRequestBuilder).execute();
		Mockito.verify(listenableActionFuture).actionGet();

		Mockito.verifyNoMoreInteractions(this.elasticSearchClient, searchRequestBuilder, listenableActionFuture);
	}

	@Test
	public void orderSpecificationDoesntContainDefaultSortFieldShouldReturnTrueWithEmptyInput() {
		Set<OrderBy> orderByList;
		boolean orderSpecificationDoesntContainDefaultSortField;
		// Given
		orderByList = null;
		// When
		orderSpecificationDoesntContainDefaultSortField = ((SearchEngineImpl) this.underTest)
				.orderSpecificationDoesntContainDefaultSortField(orderByList);
		// Then
		Assert.assertTrue(orderSpecificationDoesntContainDefaultSortField);

		// Given
		orderByList = new HashSet<OrderBy>();
		// When
		orderSpecificationDoesntContainDefaultSortField = ((SearchEngineImpl) this.underTest)
				.orderSpecificationDoesntContainDefaultSortField(orderByList);
		// Then
		Assert.assertTrue(orderSpecificationDoesntContainDefaultSortField);

		// Given
		orderByList = new HashSet<OrderBy>();
		orderByList.add(new OrderBy(null, SortDirection.ASC));
		// When
		orderSpecificationDoesntContainDefaultSortField = ((SearchEngineImpl) this.underTest)
				.orderSpecificationDoesntContainDefaultSortField(orderByList);
		// Then
		Assert.assertTrue(orderSpecificationDoesntContainDefaultSortField);

		// Given
		orderByList = new HashSet<OrderBy>();
		orderByList.add(new OrderBy("", SortDirection.ASC));
		// When
		orderSpecificationDoesntContainDefaultSortField = ((SearchEngineImpl) this.underTest)
				.orderSpecificationDoesntContainDefaultSortField(orderByList);
		// Then
		Assert.assertTrue(orderSpecificationDoesntContainDefaultSortField);

	}

	@Test
	public void orderSpecificationDoesntContainDefaultSortFieldShouldReturnFalse() {
		Set<OrderBy> orderByList;
		boolean orderSpecificationDoesntContainDefaultSortField;

		// Given
		orderByList = new HashSet<OrderBy>();
		orderByList.add(new OrderBy(OrderBy.DEFAULT_FIELD, SortDirection.ASC));
		// When
		orderSpecificationDoesntContainDefaultSortField = ((SearchEngineImpl) this.underTest)
				.orderSpecificationDoesntContainDefaultSortField(orderByList);
		// Then
		Assert.assertFalse(orderSpecificationDoesntContainDefaultSortField);

	}

	@Test
	public void orderSpecificationDoesntContainDefaultSortFieldShouldReturnTrueWithNoMatchingField() {
		Set<OrderBy> orderByList;
		boolean orderSpecificationDoesntContainDefaultSortField;

		// Given
		orderByList = new HashSet<OrderBy>();
		orderByList.add(new OrderBy(OrderBy.DEFAULT_FIELD + "a", SortDirection.ASC));
		orderByList.add(new OrderBy(OrderBy.DEFAULT_FIELD + "b", SortDirection.ASC));
		orderByList.add(new OrderBy(OrderBy.DEFAULT_FIELD + "c", SortDirection.ASC));
		// When
		orderSpecificationDoesntContainDefaultSortField = ((SearchEngineImpl) this.underTest)
				.orderSpecificationDoesntContainDefaultSortField(orderByList);
		// Then
		Assert.assertTrue(orderSpecificationDoesntContainDefaultSortField);

	}

	@Test
	public void extractSearchClausesShouldReturnNullWithEmptyInput() {

		String queryString;
		List<String> clauses;

		// Given
		queryString = null;

		// When
		clauses = ((SearchEngineImpl) this.underTest).extractSearchClauses(queryString);

		// Then
		Assert.assertNull(clauses);

		// Given
		queryString = "";

		// When
		clauses = ((SearchEngineImpl) this.underTest).extractSearchClauses(queryString);

		// Then
		Assert.assertNull(clauses);

	}

	@Test
	public void extractSearchClausesShouldSplitByPipes() {

		String clause0;
		String clause1;
		String clause2;
		String clause3;
		String queryString;
		List<String> clauses;

		// Given
		clause0 = "a";
		clause1 = "b";
		clause2 = "c";
		clause3 = "d";
		queryString = clause0.concat(SearchEngine.CLAUSES_SEPARATOR).concat(clause3)
				.concat(SearchEngine.CLAUSES_SEPARATOR).concat(clause2).concat(SearchEngine.CLAUSES_SEPARATOR)
				.concat(clause1);

		// When
		clauses = ((SearchEngineImpl) this.underTest).extractSearchClauses(queryString);

		// Then
		Assert.assertEquals(Arrays.asList(clause0, clause3, clause2, clause1), clauses);

	}

	@Test
	public void extractSearchClausesShouldExcludeSearchOperators() {

		String clause0;
		String queryString;
		List<String> clauses;

		// Given
		clause0 = SearchOperator.EXACT_MATCH_OPERATOR.toString();
		queryString = clause0;

		// When
		clauses = ((SearchEngineImpl) this.underTest).extractSearchClauses(queryString);

		// Then
		Assert.assertTrue(CollectionUtils.sizeIsEmpty(clauses));

		// Given
		clause0 = SearchOperator.FULL_TEXT_OPERATOR.toString();
		queryString = clause0;

		// When
		clauses = ((SearchEngineImpl) this.underTest).extractSearchClauses(queryString);

		// Then
		Assert.assertTrue(CollectionUtils.sizeIsEmpty(clauses));

	}

	@Test
	public void extractSearchClausesShouldExcludeTermsEndingWithSearchOperators() {

		String clause0;
		String queryString;
		List<String> clauses;

		// Given
		clause0 = "field" + SearchOperator.EXACT_MATCH_OPERATOR.toString();
		queryString = clause0;

		// When
		clauses = ((SearchEngineImpl) this.underTest).extractSearchClauses(queryString);

		// Then
		Assert.assertTrue(CollectionUtils.sizeIsEmpty(clauses));

		// Given
		clause0 = "field" + SearchOperator.FULL_TEXT_OPERATOR.toString();
		queryString = clause0;

		// When
		clauses = ((SearchEngineImpl) this.underTest).extractSearchClauses(queryString);

		// Then
		Assert.assertTrue(CollectionUtils.sizeIsEmpty(clauses));

	}

	@Test
	public void applyDefaultSortShouldSucceed() {
		// Given
		SearchRequestBuilder searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);

		// When
		((SearchEngineImpl) this.underTest).applyDefaultSort(searchRequestBuilder);

		// Then
		Mockito.verify(searchRequestBuilder).addSort(OrderBy.DEFAULT_FIELD, SortOrder.DESC);
		Mockito.verifyNoMoreInteractions(searchRequestBuilder);

	}

	@Test
	public void applySortShouldApplyDefaultSortWithEmptySortSpec() {

		Set<OrderBy> orderByList;
		SearchRequestBuilder searchRequestBuilder;

		// Given
		orderByList = null;
		searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);

		// When
		((SearchEngineImpl) this.underTest).applySort(searchRequestBuilder, orderByList);

		// Then
		Mockito.verify(searchRequestBuilder).addSort(OrderBy.DEFAULT_FIELD, SortOrder.DESC);
		Mockito.verifyNoMoreInteractions(searchRequestBuilder);

		// Given
		orderByList = new HashSet<OrderBy>();
		searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);

		// When
		((SearchEngineImpl) this.underTest).applySort(searchRequestBuilder, orderByList);

		// Then
		Mockito.verify(searchRequestBuilder).addSort(OrderBy.DEFAULT_FIELD, SortOrder.DESC);
		Mockito.verifyNoMoreInteractions(searchRequestBuilder);

	}

	@Test
	public void applySortShouldNotApplyDefaultSortIfDefaultFieldSpecified() {

		Set<OrderBy> orderByList;
		SearchRequestBuilder searchRequestBuilder;

		// Given
		orderByList = new HashSet<OrderBy>();
		orderByList.add(new OrderBy(OrderBy.DEFAULT_FIELD, SortDirection.ASC));
		searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);

		// When
		((SearchEngineImpl) this.underTest).applySort(searchRequestBuilder, orderByList);

		// Then
		Mockito.verify(searchRequestBuilder).addSort(OrderBy.DEFAULT_FIELD, SortOrder.ASC);
		Mockito.verifyNoMoreInteractions(searchRequestBuilder);

	}

	@Test
	public void applySortShouldSucceed() {

		Set<OrderBy> orderByList;
		SearchRequestBuilder searchRequestBuilder;

		// Given
		orderByList = new HashSet<OrderBy>();
		String field = "field";
		orderByList.add(new OrderBy(field, SortDirection.ASC));
		searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);

		// When
		((SearchEngineImpl) this.underTest).applySort(searchRequestBuilder, orderByList);

		// Then
		Mockito.verify(searchRequestBuilder).addSort(field, SortOrder.ASC);
		Mockito.verify(searchRequestBuilder).addSort(OrderBy.DEFAULT_FIELD, SortOrder.DESC);
		Mockito.verifyNoMoreInteractions(searchRequestBuilder);

	}

	@Test
	public void resolveQueryBuilderShouldReturnAllResults() {

		List<String> clauses;
		QueryBuilder queryBuilder;

		// Given
		clauses = null;

		// When
		queryBuilder = ((SearchEngineImpl) this.underTest).resolveQueryBuilder(clauses);

		// Then
		Assert.assertTrue(queryBuilder instanceof MatchAllQueryBuilder);

		// Given
		clauses = Collections.emptyList();

		// When
		queryBuilder = ((SearchEngineImpl) this.underTest).resolveQueryBuilder(clauses);

		// Then
		Assert.assertTrue(queryBuilder instanceof MatchAllQueryBuilder);

	}

	@Test
	public void resolveQueryBuilderShouldReturnFullTextSearchOnAllAnalysedFields() {

		List<String> clauses;
		QueryBuilder queryBuilder;

		// Given
		clauses = Arrays.asList("this is a text with no operator");

		// When
		queryBuilder = ((SearchEngineImpl) this.underTest).resolveQueryBuilder(clauses);

		// Then
		Assert.assertTrue(queryBuilder instanceof QueryStringQueryBuilder);

	}

	@Test
	public void resolveQueryBuilderShouldReturnFullTextSearchOnSpecifiedAnalysedField() {

		List<String> clauses;
		QueryBuilder queryBuilder;

		// Given
		clauses = Arrays.asList("field~this is a clause with full text operator");

		// When
		queryBuilder = ((SearchEngineImpl) this.underTest).resolveQueryBuilder(clauses);

		// Then
		Assert.assertTrue(queryBuilder instanceof BoolQueryBuilder);

	}

	@Test
	public void convertSearchQueryToRequestBuilderShouldSucceed() {

		SearchRequestBuilder searchRequestBuilder;
		SearchQuery query;
		String sortField;
		SortDirection sortDirection;
		int from;

		// Given
		sortField = "created";
		sortDirection = SortDirection.ASC;
		OrderBy orderBy = new OrderBy(sortField, sortDirection);
		Set<OrderBy> orderByList = new HashSet<OrderBy>();
		orderByList.add(orderBy);
		String clause0 = "status:draft";
		String clause1 = "updated:2012-01-15";
		String clause2 = "description~this is an analyzed text on which we can operate a full text search";

		String queryString = new StringBuilder(clause0).append(SearchEngine.CLAUSES_SEPARATOR).append(clause1)
				.append(SearchEngine.CLAUSES_SEPARATOR).append(clause2).toString();

		query = new SearchQuery(queryString, orderByList);
		from = query.getStartPage() - 1;
		searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);
		Mockito.when(this.elasticSearchClient.prepareSearch(Utils.pluralize(Advert.class))).thenReturn(
				searchRequestBuilder);
		Mockito.when(searchRequestBuilder.setTypes(Utils.minimize(Advert.class))).thenReturn(searchRequestBuilder);
		Mockito.when(searchRequestBuilder.setFrom(from)).thenReturn(searchRequestBuilder);
		Mockito.when(searchRequestBuilder.setSize(query.getItemsPerPage())).thenReturn(searchRequestBuilder);
		Mockito.when(searchRequestBuilder.setQuery(Matchers.any(QueryBuilder.class))).thenReturn(searchRequestBuilder);
		Mockito.when(searchRequestBuilder.addSort(Matchers.anyString(), Matchers.any(SortOrder.class))).thenReturn(
				searchRequestBuilder);

		// When
		SearchRequestBuilder actualRearchRequestBuilder = ((SearchEngineImpl) this.underTest)
				.convertSearchQueryToRequestBuilder(query);

		// Then
		Assert.assertSame(actualRearchRequestBuilder, searchRequestBuilder);
		Mockito.verify(this.elasticSearchClient).prepareSearch(Utils.pluralize(Advert.class));
		Mockito.verify(searchRequestBuilder).setTypes(Utils.minimize(Advert.class));
		Mockito.verify(searchRequestBuilder).setFrom(from);
		Mockito.verify(searchRequestBuilder).setSize(query.getItemsPerPage());
		Mockito.verify(searchRequestBuilder).setQuery(Matchers.any(QueryBuilder.class));
		Mockito.verify(searchRequestBuilder).addSort(sortField, SortOrder.ASC);
		Mockito.verify(searchRequestBuilder).addSort(OrderBy.DEFAULT_FIELD, SortOrder.DESC);
	}
}
