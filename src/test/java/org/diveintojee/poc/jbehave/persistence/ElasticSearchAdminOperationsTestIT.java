package org.diveintojee.poc.jbehave.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.elasticsearch.client.Client;
import org.elasticsearch.indices.IndexMissingException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("classpath:jbehave-poc-server.xml")
public class ElasticSearchAdminOperationsTestIT extends AbstractJUnit4SpringContextTests {

    @Autowired
    private Client underTest;

    @Before
    public void before() {
        assertNotNull(underTest);
        if (underTest.admin().indices().prepareExists(Utils.pluralize(Advert.class)).execute().actionGet().exists()) {
            underTest.admin().indices().prepareDelete(Utils.pluralize(Advert.class)).execute().actionGet();
        }
    }

    @Test
    public void crudOnIndexShouldSucceed() {

        // Ensure the index doesn't exist
        try {
            // When I refresh the index
            underTest.admin().indices().prepareRefresh(Utils.pluralize(Advert.class)).execute().actionGet();
            fail(IndexMissingException.class + " expected");
        } catch (final IndexMissingException throwable) {
            // Then I should fail because the index does not exist
        }

        // When I create that index
        underTest.admin().indices().prepareCreate(Utils.pluralize(Advert.class)).execute().actionGet();
        // Then I should be able to refresh it
        underTest.admin().indices().prepareRefresh(Utils.pluralize(Advert.class)).execute().actionGet();

        // Given I delete the index
        underTest.admin().indices().prepareDelete(Utils.pluralize(Advert.class)).execute().actionGet();
        try {
            // When I refresh the index
            underTest.admin().indices().prepareRefresh(Utils.pluralize(Advert.class)).execute().actionGet();
            fail(IndexMissingException.class + " expected");
        } catch (final IndexMissingException throwable) {
            // Then I should fail because the index was deleted
        }

    }
}
