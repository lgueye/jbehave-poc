/*
 *
 */
package org.diveintojee.poc.jbehave.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.diveintojee.poc.jbehave.domain.business.Facade;
import org.diveintojee.poc.jbehave.persistence.JsonByteArrayToAdvertConverter;
import org.diveintojee.poc.jbehave.persistence.SearchEngine;
import org.diveintojee.poc.jbehave.test.TestUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jbehave-poc-server.xml" })
public class FacadeImplTestIT {

    @Autowired
    private Facade underTest;

    @Autowired
    private SearchEngine searchEngine;

    @Autowired
    @Qualifier(JsonByteArrayToAdvertConverter.BEAN_ID)
    private Converter<byte[], Advert> jsonByteArrayToAdvertConverter;

    private void assertHitsCount(final int expectedHitsCount, final SearchResponse actualResponse) {

        assertNotNull(actualResponse);

        final SearchHits hits = actualResponse.getHits();

        assertNotNull(hits);

        final int totalHits = (int) hits.getTotalHits();

        assertTrue(expectedHitsCount == totalHits);

    }

    @Before
    public void before() {

        assertNotNull(searchEngine);

        // delete if already exists
        if (searchEngine.getClient().admin().indices().prepareExists(Utils.pluralize(Advert.class)).execute()
                .actionGet().exists()) {
            searchEngine.getClient().admin().indices().prepareDelete(Utils.pluralize(Advert.class)).execute()
                    .actionGet();
        }

        // create it
        searchEngine.getClient().admin().indices().prepareCreate(Utils.pluralize(Advert.class)).execute().actionGet();

    }

    @Test
    public void createEntityShouldPersistAndSetId() throws Throwable {
        // Given
        final Advert advert = TestUtils.validAdvert();
        // ensure id nullity
        advert.setId(null);
        // When
        final Long id = underTest.createAdvert(advert);

        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, advert.getId());

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
        advert.setEmail("test@test.com");
        advert.setName("Bike to sell.");
        advert.setDescription("Bike to sell. Nearly never used. You got a real deal here. 4500€");
        underTest.createAdvert(advert);
        expectedHitsCount = 1;
        // When I search that data by id
        actualResponse = findById(id);
        // Then I should get 1 hit
        assertHitsCount(expectedHitsCount, actualResponse);

        // Given a reference
        reference = "REF-0000-54-ADV";
        expectedHitsCount = 1;
        // When I update the name of the data
        advert.setReference(reference);
        // And I reindex that data
        underTest.updateAdvert(advert);
        // And I search that data by id
        actualResponse = findById(id);
        // Then the name should be modified
        assertHitsCount(expectedHitsCount, actualResponse);
        advert = extractAdvertFromResponse(actualResponse);
        assertEquals(reference, advert.getReference());

        // Given I delete a data from the index
        underTest.deleteAdvert(id);
        expectedHitsCount = 0;
        // When I search that data by id
        actualResponse = findById(id);
        // Then I should get 0 hit
        assertHitsCount(expectedHitsCount, actualResponse);

    }

    @Test
    public void deleteEntityShouldSucceed() throws Throwable {
        // Given
        final Advert advert = TestUtils.validAdvert();
        advert.setId(null);
        // When
        final Long id = underTest.createAdvert(advert);
        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, advert.getId());

        // When
        underTest.deleteAdvert(advert.getId());

        // Then
        Assert.assertNull(underTest.readAdvert(id));
    }

    /**
     * @param actualResponse
     * @return
     */
    private Advert extractAdvertFromResponse(final SearchResponse actualResponse) {

        assertNotNull(actualResponse);

        assertNotNull(actualResponse.getHits());

        final SearchHits hits = actualResponse.getHits();

        assertEquals(1, hits.getTotalHits());

        final SearchHit hit = hits.getHits()[0];

        assertNotNull(hit);

        assertNotNull(hit.source());

        final Advert advert = jsonByteArrayToAdvertConverter.convert(hit.source());

        return advert;

    }

    private SearchResponse findById(final Long id) {
        return searchEngine.getClient().prepareSearch(Utils.pluralize(Advert.class))
                .setTypes(Utils.minimize(Advert.class))
                .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("_id", id))).execute().actionGet();
    }

    @Test
    public void updateEntityShouldPersistProperties() throws Throwable {
        // Given
        Advert advert = TestUtils.validAdvert();
        advert.setId(null);
        // When
        final Long id = underTest.createAdvert(advert);
        // Then
        Assert.assertNotNull(id);
        Assert.assertEquals(id, advert.getId());
        final String newName = "New name";
        final String newDescription = "Brand New description";

        // Given
        advert.setName(newName);
        advert.setDescription(newDescription);

        // When
        underTest.updateAdvert(advert);

        advert = underTest.readAdvert(id);

        // Then
        Assert.assertEquals(newName, advert.getName());
        Assert.assertEquals(newDescription, advert.getDescription());

    }

}
