/*
 *
 */
package org.diveintojee.poc.jbehave.domain;

/**
 * @author louis.gueye@gmail.com
 */
public enum SortDirection {
    /**
     * Ascending order.
     */
    ASC {
        @Override
        public String toString() {
            return "asc";
        }
    },
    /**
     * Descending order.
     */
    DESC {
        @Override
        public String toString() {
            return "desc";
        }
    }
}
