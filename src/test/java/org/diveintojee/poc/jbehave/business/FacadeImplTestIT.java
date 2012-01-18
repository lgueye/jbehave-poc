/*
 *
 */
package org.diveintojee.poc.jbehave.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.OrderBy;
import org.diveintojee.poc.jbehave.domain.SearchResult;
import org.diveintojee.poc.jbehave.domain.SortDirection;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
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

    @Value("classpath:elasticsearch/mappings/advert.json")
    private Resource mapping;

    private void assertHitsCount(final int expectedHitsCount, final SearchResponse actualResponse) {

        assertNotNull(actualResponse);

        final SearchHits hits = actualResponse.getHits();

        assertNotNull(hits);

        final int totalHits = (int) hits.getTotalHits();

        assertTrue(expectedHitsCount == totalHits);

    }

    @Before
    public void before() throws IOException {

        assertNotNull(searchEngine);

        // delete if already exists
        if (searchEngine.getClient().admin().indices().prepareExists(Utils.pluralize(Advert.class)).execute()
                .actionGet().exists()) {
            searchEngine.getClient().admin().indices().prepareDelete(Utils.pluralize(Advert.class)).execute()
                    .actionGet();
        }

        // create it
        searchEngine.getClient().admin().indices().prepareCreate(Utils.pluralize(Advert.class)).execute().actionGet();

        final Writer writer = new StringWriter();
        IOUtils.copy(mapping.getInputStream(), writer, "UTF-8");
        searchEngine.getClient().admin().indices().preparePutMapping(Utils.pluralize(Advert.class))
                .setSource(writer.toString()).setType(Utils.minimize(Advert.class)).execute().actionGet();

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
        Long id;
        int expectedHitsCount;
        SearchResponse actualResponse;
        Advert advert;
        String reference;

        // Given I create that data
        advert = TestUtils.validAdvert();
        advert.setEmail("test@test.com");
        advert.setName("Bike to sell.");
        advert.setDescription("Bike to sell. Nearly never used. You got a real deal here. 4500€");
        id = underTest.createAdvert(advert);
        expectedHitsCount = 1;
        // When I search that data by id
        actualResponse = findById(advert.getId());
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

    /**
     * @param ids
     */
    private void deleteAdverts(final List<Long> ids) {
        for (final Long id : ids) {
            underTest.deleteAdvert(id);
        }
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

    @Test
    public void findByEmailShouldSucceed() {
        // Variables
        String queryString;
        Set<OrderBy> orderByList;
        final int startPage;
        final int itemsPerPage;

        // Given
        final List<Long> ids = insertSearchableAdverts();

        queryString = "email:foo009@bar.com";
        orderByList = new HashSet<OrderBy>(Arrays.asList(new OrderBy("reference", SortDirection.DESC)));
        startPage = 1;
        itemsPerPage = 2;

        // When
        final SearchResult results = underTest.findAdvertsByCriteria(queryString, orderByList, startPage, itemsPerPage);

        // Then
        assertNotNull(results);
        assertEquals(2, results.getTotalHits());
        assertEquals(results.getTotalHits(), results.getItems().size());

        deleteAdverts(ids);
    }

    private SearchResponse findById(final Long id) {
        return searchEngine.getClient().prepareSearch(Utils.pluralize(Advert.class))
                .setTypes(Utils.minimize(Advert.class))
                .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("_id", id))).execute().actionGet();
    }

    /**
     * @return
     */
    private List<Long> insertSearchableAdverts() {

        final List<Long> ids = new ArrayList<Long>();
        Advert advert;

        advert = new Advert();
        advert.getAddress().setCity("Paris 9ème");
        advert.getAddress().setCountryCode("fr");
        advert.getAddress().setPostalCode("75009");
        advert.getAddress().setStreetAddress("3 rue Lafayette");
        advert.setDescription("Baby phone CHICCO, scope 30m, excellent condition, 10€, pick up on site only");
        advert.setEmail("foo009@bar.com");
        advert.setName("Baby phone TBE");
        advert.setReference("REF-75009-001");
        advert.setPhoneNumber("060965234102");
        ids.add(underTest.createAdvert(advert));

        advert = new Advert();
        advert.getAddress().setCity("Paris 9ème");
        advert.getAddress().setCountryCode("fr");
        advert.getAddress().setPostalCode("75009");
        advert.getAddress().setStreetAddress("10 rue Le peletier");
        advert.setDescription("JANE twin stroller, good condition, 120€, pick up on site only");
        advert.setEmail("foo009@bar.com");
        advert.setName("Twin stroller");
        advert.setReference("978-2253170570");
        advert.setPhoneNumber("060968634102");
        ids.add(underTest.createAdvert(advert));

        return ids;
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
