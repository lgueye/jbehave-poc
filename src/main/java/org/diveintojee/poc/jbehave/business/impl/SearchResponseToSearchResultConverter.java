/**
 *
 */
package org.diveintojee.poc.jbehave.business.impl;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.SearchResult;
import org.diveintojee.poc.jbehave.persistence.JsonByteArrayToAdvertConverter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(SearchResponseToSearchResultConverter.BEAN_ID)
public class SearchResponseToSearchResultConverter implements Converter<SearchResponse, SearchResult> {

    public static final String BEAN_ID = "searchResponseToSearchResultConverter";

    @Autowired
    @Qualifier(JsonByteArrayToAdvertConverter.BEAN_ID)
    private Converter<byte[], Advert> jsonByteArrayToAdvertConverter;

    /**
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public SearchResult convert(final SearchResponse source) {

        final SearchResult result = new SearchResult();

        final SearchHits hits = source.getHits();

        result.setTotalHits(hits.getTotalHits());

        for (final SearchHit searchHit : hits.getHits()) {

            final Advert advert = jsonByteArrayToAdvertConverter.convert(searchHit.source());

            result.addItem(advert);

        }

        return result;

    }

}
