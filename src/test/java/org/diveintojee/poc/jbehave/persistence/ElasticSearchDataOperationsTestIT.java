package org.diveintojee.poc.jbehave.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.test.TestUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("classpath:jbehave-poc-elasticsearch.xml")
public class ElasticSearchDataOperationsTestIT extends AbstractJUnit4SpringContextTests {

    @Autowired
    private Client underTest;

    @Autowired
    @Qualifier(AdvertToJsonByteArrayConverter.BEAN_ID)
    private Converter<Advert, byte[]> advertToJsonByteArrayConverter;

    @Autowired
    @Qualifier(JsonByteArrayToAdvertConverter.BEAN_ID)
    private Converter<Advert, byte[]> jsonByteArrayToAdvertConverter;

    private void assertHitsCount(final int expectedHitsCount, final SearchResponse actualResponse) {

        assertNotNull(actualResponse);

        final SearchHits hits = actualResponse.getHits();

        assertNotNull(hits);

        final int totalHits = (int) hits.getTotalHits();

        assertTrue(expectedHitsCount == totalHits);

    }

    @Before
    public void before() {
        assertNotNull(underTest);

        // delete if already exists
        if (underTest.admin().indices().prepareExists(PersistenceConstants.ADVERTS_INDEX_NAME).execute().actionGet()
                .exists()) {
            underTest.admin().indices().prepareDelete(PersistenceConstants.ADVERTS_INDEX_NAME).execute().actionGet();
        }

        // create it
        underTest.admin().indices().prepareCreate(PersistenceConstants.ADVERTS_INDEX_NAME).execute().actionGet();

    }

    @Test
    public void crudOnDataShouldSucceed() {
        final Long id = 8L;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Advert advert;
        String reference;

        // Given
        expectedHitsCount = 0;
        // When I search data by id
        actualResponse = findById(id);
        // Then I should get 0 hits
        assertHitsCount(expectedHitsCount, actualResponse);

        // Given I create that data
        advert = TestUtils.validAdvert();
        indexAdvert(id, advert);
        expectedHitsCount = 1;
        // When I search that data by id
        actualResponse = findById(id);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);

        // Given a reference
        reference = "REF-0000-54-ADV";
        // When I update the name of the data
        advert.setReference(reference);
        // And I reindex that data
        indexAdvert(id, advert);
        // When I search that data by id
        actualResponse = findById(id);
        // Then the name should be modified
        advert = extractAdvertFromResponse(actualResponse);
        assertEquals(reference, advert.getReference());

        // Given I delete a data from the index
        deleteAdvertFromIndex(id);
        expectedHitsCount = 0;
        // When I search that data by id
        actualResponse = findById(id);
        // Then I should get 0 hit
        assertHitsCount(expectedHitsCount, actualResponse);

    }

    /**
     * @param id
     */
    private void deleteAdvertFromIndex(final Long id) {

    }

    /**
     * @param actualResponse
     * @return
     */
    private Advert extractAdvertFromResponse(final SearchResponse actualResponse) {
        // TODO Auto-generated method stub
        return null;
    }

    private SearchResponse findById(final Long id) {
        return underTest.prepareSearch(PersistenceConstants.ADVERTS_INDEX_NAME)
                .setTypes(PersistenceConstants.ADVERTS_TYPE_NAME)
                .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("id", id))).execute().actionGet();
    }

    /**
     * @param advert
     */
    private void indexAdvert(final Long id, final Advert advert) {
        underTest
                .prepareIndex(PersistenceConstants.ADVERTS_INDEX_NAME, PersistenceConstants.ADVERTS_TYPE_NAME,
                    id.toString())//
                .setRefresh(true) //
                .setSource(advertToJsonByteArrayConverter.convert(advert)) //
                .execute().actionGet();

    }
}
