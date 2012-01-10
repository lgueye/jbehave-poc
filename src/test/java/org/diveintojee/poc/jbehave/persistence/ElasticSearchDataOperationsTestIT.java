package org.diveintojee.poc.jbehave.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("classpath:jbehave-poc-server.xml")
public class ElasticSearchDataOperationsTestIT extends AbstractJUnit4SpringContextTests {

	@Autowired
	private Client						underTest;

	@Autowired
	@Qualifier(AdvertToJsonByteArrayConverter.BEAN_ID)
	private Converter<Advert, byte[]>	advertToJsonByteArrayConverter;

	@Autowired
	@Qualifier(JsonByteArrayToAdvertConverter.BEAN_ID)
	private Converter<byte[], Advert>	jsonByteArrayToAdvertConverter;

	private void assertHitsCount(final int expectedHitsCount, final SearchResponse actualResponse) {

		assertNotNull(actualResponse);

		final SearchHits hits = actualResponse.getHits();

		assertNotNull(hits);

		final int totalHits = (int) hits.getTotalHits();

		assertTrue(expectedHitsCount == totalHits);

	}

	@Before
	public void before() {

		assertNotNull(this.underTest);

		// delete if already exists
		if (this.underTest.admin().indices().prepareExists(PersistenceConstants.ADVERTS_INDEX_NAME).execute()
				.actionGet().exists()) this.underTest.admin().indices()
				.prepareDelete(PersistenceConstants.ADVERTS_INDEX_NAME).execute().actionGet();

		// create it
		this.underTest.admin().indices().prepareCreate(PersistenceConstants.ADVERTS_INDEX_NAME).execute().actionGet();

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
		advert = new Advert();
		advert.setEmail("test@test.com");
		advert.setName("Bike to sell.");
		advert.setDescription("Bike to sell. Nearly never used. You got a real deal here. 4500€");
		indexAdvert(id, advert);
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
		indexAdvert(id, advert);
		// And I search that data by id
		actualResponse = findById(id);
		// Then the name should be modified
		assertHitsCount(expectedHitsCount, actualResponse);
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
		this.underTest
				.prepareDelete(PersistenceConstants.ADVERTS_INDEX_NAME, PersistenceConstants.ADVERTS_TYPE_NAME,
						id.toString()).setRefresh(true).execute().actionGet();
	}

	/**
	 * @param actualResponse
	 * @return
	 */
	private Advert extractAdvertFromResponse(final SearchResponse actualResponse) {

		assertNotNull(actualResponse);

		assertNotNull(actualResponse.getHits());

		SearchHits hits = actualResponse.getHits();

		assertEquals(1, hits.getTotalHits());

		SearchHit hit = hits.getHits()[0];

		assertNotNull(hit);

		assertNotNull(hit.source());

		Advert advert = this.jsonByteArrayToAdvertConverter.convert(hit.source());

		return advert;

	}

	private SearchResponse findById(final Long id) {
		return this.underTest.prepareSearch(PersistenceConstants.ADVERTS_INDEX_NAME)
				.setTypes(PersistenceConstants.ADVERTS_TYPE_NAME)
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("_id", id))).execute().actionGet();
	}

	/**
	 * @param advert
	 */
	private void indexAdvert(final Long id, final Advert advert) {
		this.underTest
				.prepareIndex(PersistenceConstants.ADVERTS_INDEX_NAME, PersistenceConstants.ADVERTS_TYPE_NAME,
						id.toString())//
				.setRefresh(true) //
				.setSource(this.advertToJsonByteArrayConverter.convert(advert)) //
				.execute().actionGet();

	}
}
