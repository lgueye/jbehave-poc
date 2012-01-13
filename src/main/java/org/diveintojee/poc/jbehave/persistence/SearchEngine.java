/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.SearchQuery;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;

/**
 * @author louis.gueye@gmail.com
 */
public interface SearchEngine {

    /**
     * @param type
     * @param id
     * @return
     */
    SearchResponse findById(Class<?> type, Long id);

    /**
     * @return
     */
    Client getClient();

    /**
     * @param type
     * @param document
     */
    void index(Class<?> type, Object document);

    /**
     * @param type
     * @param id
     */
    void removeFromIndex(Class<?> type, Long id);

    SearchResponse search(SearchQuery searchQuery);
}
