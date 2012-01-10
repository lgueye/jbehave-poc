/*
 *
 */
package org.diveintojee.poc.jbehave.persistence.impl;

import org.diveintojee.poc.jbehave.domain.Address;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.Persistable;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.diveintojee.poc.jbehave.persistence.SearchEngine;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.action.delete.DeleteRequestBuilder;
import org.elasticsearch.client.action.index.IndexRequestBuilder;
import org.elasticsearch.client.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchEngineImplTest {

    @Mock
    private Client elasticSearchClient;

    @Mock
    private Converter<Advert, byte[]> advertToJsonByteArrayConverter;

    @InjectMocks
    private final SearchEngine underTest = new SearchEngineImpl();

    @Test(expected = IllegalArgumentException.class)
    public void findByIdWillThrowIllegalArgumentExceptionWithNullId() {
        underTest.findById(Advert.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByIdWillThrowIllegalArgumentExceptionWithNullType() {
        underTest.findById(null, 6L);
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
        @SuppressWarnings("unchecked") final ListenableActionFuture<IndexResponse> listenableActionFuture = Mockito
                .mock(ListenableActionFuture.class);
        Mockito.when(
            elasticSearchClient.prepareIndex(Utils.pluralize(type), Utils.minimize(type), ((Persistable) document)
                    .getId().toString())).thenReturn(indexRequestBuilder);
        Mockito.when(indexRequestBuilder.setRefresh(true)).thenReturn(indexRequestBuilder);
        Mockito.when(advertToJsonByteArrayConverter.convert(document)).thenReturn(documentAsBytes);
        Mockito.when(indexRequestBuilder.setSource(documentAsBytes)).thenReturn(indexRequestBuilder);
        Mockito.when(indexRequestBuilder.execute()).thenReturn(listenableActionFuture);

        // When
        underTest.index(type, document);

        // Then
        Mockito.verify(elasticSearchClient).prepareIndex(Utils.pluralize(type), Utils.minimize(type),
            ((Persistable) document).getId().toString());
        Mockito.verify(indexRequestBuilder).setRefresh(true);
        Mockito.verify(advertToJsonByteArrayConverter).convert(document);
        Mockito.verify(indexRequestBuilder).setSource(documentAsBytes);
        Mockito.verify(indexRequestBuilder).execute();
        Mockito.verify(listenableActionFuture).actionGet();

        Mockito.verifyNoMoreInteractions(elasticSearchClient, indexRequestBuilder, advertToJsonByteArrayConverter,
            listenableActionFuture);
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexWillThrowIllegalArgumentExceptionIfDocumentIsNotAnAdvert() {
        underTest.index(Address.class, new Address());
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexWillThrowIllegalArgumentExceptionWithNullDocument() {
        underTest.index(Advert.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexWillThrowIllegalArgumentExceptionWithNullDocumentId() {
        underTest.index(Advert.class, new Advert());
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexWillThrowIllegalArgumentExceptionWithNullType() {
        underTest.index(null, new Advert());
    }

    @Test
    public void removeFromIndexWillInvokeElasticSearch() {
        // Given
        final Class<?> type = Advert.class;
        final Long id = 5L;
        final DeleteRequestBuilder deleteRequestBuilder = Mockito.mock(DeleteRequestBuilder.class);
        @SuppressWarnings("unchecked") final ListenableActionFuture<DeleteResponse> listenableActionFuture = Mockito
                .mock(ListenableActionFuture.class);
        Mockito.when(elasticSearchClient.prepareDelete(Utils.pluralize(type), Utils.minimize(type), id.toString()))
                .thenReturn(deleteRequestBuilder);
        Mockito.when(deleteRequestBuilder.setRefresh(true)).thenReturn(deleteRequestBuilder);
        Mockito.when(deleteRequestBuilder.execute()).thenReturn(listenableActionFuture);

        // When
        underTest.removeFromIndex(type, id);

        // Then
        Mockito.verify(elasticSearchClient).prepareDelete(Utils.pluralize(type), Utils.minimize(type), id.toString());
        Mockito.verify(deleteRequestBuilder).setRefresh(true);
        Mockito.verify(deleteRequestBuilder).execute();
        Mockito.verify(listenableActionFuture).actionGet();
        Mockito.verifyNoMoreInteractions(elasticSearchClient, deleteRequestBuilder, listenableActionFuture);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeFromIndexWillThrowIllegalArgumentExceptionWithNullId() {
        underTest.removeFromIndex(Advert.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeFromIndexWillThrowIllegalArgumentExceptionWithNullType() {
        underTest.removeFromIndex(null, 6L);
    }

    @Test
    public void searchWillInvokeElasticSearch() {
        // Given
        final Class<?> type = Advert.class;
        final Long id = 5L;
        final SearchRequestBuilder searchRequestBuilder = Mockito.mock(SearchRequestBuilder.class);
        @SuppressWarnings("unchecked") final ListenableActionFuture<SearchResponse> listenableActionFuture = Mockito
                .mock(ListenableActionFuture.class);
        Mockito.when(elasticSearchClient.prepareSearch(Utils.pluralize(type))).thenReturn(searchRequestBuilder);
        Mockito.when(searchRequestBuilder.setTypes(Utils.minimize(type))).thenReturn(searchRequestBuilder);
        Mockito.when(searchRequestBuilder.setQuery(Mockito.any(QueryBuilder.class))).thenReturn(searchRequestBuilder);
        Mockito.when(searchRequestBuilder.execute()).thenReturn(listenableActionFuture);

        // When
        underTest.findById(type, id);
        // Then
        Mockito.verify(elasticSearchClient).prepareSearch(Utils.pluralize(type));
        Mockito.verify(searchRequestBuilder).setTypes(Utils.minimize(type));
        Mockito.verify(searchRequestBuilder).setQuery(Mockito.any(QueryBuilder.class));
        Mockito.verify(searchRequestBuilder).execute();
        Mockito.verify(listenableActionFuture).actionGet();

        Mockito.verifyNoMoreInteractions(elasticSearchClient, searchRequestBuilder, listenableActionFuture);
    }
}
