/*
 *
 */
package org.diveintojee.poc.jbehave.domain;

import java.util.Set;


/**
 * @author louis.gueye@gmail.com
 */
public class SearchQuery extends AbstractObject {

    private int itemsPerPage;
    private Set<OrderBy> orderByList;
    private String queryString;
    private int startPage;

    /**
     * @param itemsPerPage
     * @param orderByList
     * @param queryString
     * @param startPage
     */
    public SearchQuery(final int itemsPerPage, final Set<OrderBy> orderByList, final String queryString,
            final int startPage) {
        super();
        this.itemsPerPage = itemsPerPage;
        this.orderByList = orderByList;
        this.queryString = queryString;
        this.startPage = startPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public Set<OrderBy> getOrderByList() {
        return orderByList;
    }

    public String getQueryString() {
        return queryString;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setItemsPerPage(final int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void setOrderByList(final Set<OrderBy> orderByList) {
        this.orderByList = orderByList;
    }

    public void setQueryString(final String queryString) {
        this.queryString = queryString;
    }

    public void setStartPage(final int startPage) {
        this.startPage = startPage;
    }
}
