/*
 *
 */
package org.diveintojee.poc.jbehave.persistence.impl;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.Persistable;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.diveintojee.poc.jbehave.persistence.AdvertToJsonByteArrayConverter;
import org.diveintojee.poc.jbehave.persistence.SearchEngine;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class SearchEngineImpl implements SearchEngine {

    @Autowired
    private Client elasticSearchClient;

    @Autowired
    @Qualifier(AdvertToJsonByteArrayConverter.BEAN_ID)
    private Converter<Advert, byte[]> advertToJsonByteArrayConverter;

    /**
     * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#findById(java.lang.String, java.lang.Class,
     *      java.lang.Long)
     */
    @Override
    public SearchResponse findById(final Class<?> type, final Long id) {

        Preconditions.checkArgument(type != null, "type is required");

        Preconditions.checkArgument(id != null, "id is required");

        final TermQueryBuilder idClause = QueryBuilders.termQuery("_id", id);

        final QueryBuilder builder = QueryBuilders.boolQuery().must(idClause);

        return elasticSearchClient.prepareSearch(Utils.pluralize(type)).setTypes(Utils.minimize(type))
                .setQuery(builder).execute().actionGet();
    }

    /**
     * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#index(java.lang.String, java.lang.Class,
     *      java.lang.Object)
     */
    @Override
    public void index(final Class<?> type, final Object document) {

        Preconditions.checkArgument(type != null, "type is required");

        Preconditions.checkArgument(document != null, "document is required");

        Preconditions.checkArgument(document instanceof Advert, "advert document is required");

        Preconditions.checkArgument(((Persistable) document).getId() != null, "document id is required");

        elasticSearchClient
                .prepareIndex(Utils.pluralize(type), Utils.minimize(type), ((Persistable) document).getId().toString())//
                .setRefresh(true) //
                .setSource(advertToJsonByteArrayConverter.convert((Advert) document)) //
                .execute().actionGet();
    }

    /**
     * @see org.diveintojee.poc.jbehave.persistence.SearchEngine#removeFromIndex(java.lang.String, java.lang.Class,
     *      java.lang.Long)
     */
    @Override
    public void removeFromIndex(final Class<?> type, final Long id) {

        Preconditions.checkArgument(type != null, "type is required");

        Preconditions.checkArgument(id != null, "Id is required");

        elasticSearchClient.prepareDelete(Utils.pluralize(type), Utils.minimize(type), id.toString()).setRefresh(true)
                .execute().actionGet();
    }

}
