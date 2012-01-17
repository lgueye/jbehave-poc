/*
 *
 */
package org.diveintojee.poc.jbehave.business.impl;

import java.io.UnsupportedEncodingException;

import org.apache.commons.collections.CollectionUtils;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.SearchResult;
import org.diveintojee.poc.jbehave.persistence.JsonByteArrayToAdvertConverter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Assert;
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
public class SearchResponseToSearchResultConverterTest {

    @Mock
    private JsonByteArrayToAdvertConverter jsonByteArrayToadvertConverter;

    @InjectMocks
    private final Converter<SearchResponse, SearchResult> underTest = new SearchResponseToSearchResultConverter();

    /**
     * Test method for
     * {@link org.diveintojee.poc.jbehave.business.impl.SearchResponseToSearchResultConverter#convert(org.elasticsearch.action.search.SearchResponse)}
     * .
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public final void searchResponseShould() throws UnsupportedEncodingException {

        SearchResponse searchResponse;
        SearchHits searchHits;
        SearchHit[] hitsArray;
        SearchHit searchHit;
        byte[] byteArray;
        Advert advert;
        SearchResult result;

        // Given
        searchResponse = Mockito.mock(SearchResponse.class);
        searchHits = Mockito.mock(SearchHits.class);
        searchHit = Mockito.mock(SearchHit.class);
        hitsArray = new SearchHit[] { searchHit };
        byteArray = "".getBytes("utf-8");
        advert = Mockito.mock(Advert.class);
        Mockito.when(searchResponse.getHits()).thenReturn(searchHits);
        Mockito.when(searchHits.getHits()).thenReturn(hitsArray);
        Mockito.when(searchHit.source()).thenReturn(byteArray);
        Mockito.when(jsonByteArrayToadvertConverter.convert(byteArray)).thenReturn(advert);

        // When
        result = underTest.convert(searchResponse);

        // Then
        Mockito.verify(searchResponse).getHits();
        Assert.assertNotNull(result);
        Assert.assertEquals(searchHits.getTotalHits(), result.getTotalHits());
        Assert.assertTrue(CollectionUtils.size(result.getItems()) == 1);
        Assert.assertEquals(advert, result.getItems().get(0));

    }

}
