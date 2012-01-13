package org.diveintojee.poc.jbehave.persistence;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.fail;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.Utils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.IndexMissingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@ContextConfiguration("classpath:jbehave-poc-server.xml")
public class ElasticSearchAdminOperationsTestIT extends AbstractNodesTests {

    private Client underTest;

    @After
    public void closeNodes() {
        underTest.close();
        closeAllNodes();
    }

    @Before
    public void createNodes() throws Exception {
        final Settings settings = settingsBuilder().put("number_of_shards", 3).put("number_of_replicas", 0).build();
        startNode(ElasticSearchAdminOperationsTestIT.class.getSimpleName(), settings);
        underTest = getClient();
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

    protected Client getClient() {
        return client(ElasticSearchAdminOperationsTestIT.class.getSimpleName());
    }
}
