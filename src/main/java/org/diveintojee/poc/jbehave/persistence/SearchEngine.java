/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.elasticsearch.action.search.SearchResponse;

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
     * @param type
     * @param document
     */
    void index(Class<?> type, Object document);

    /**
     * @param type
     * @param id
     */
    void removeFromIndex(Class<?> type, Long id);

}
