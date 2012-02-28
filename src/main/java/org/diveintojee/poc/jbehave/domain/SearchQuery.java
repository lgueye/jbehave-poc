/*
 *
 */
package org.diveintojee.poc.jbehave.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * @author louis.gueye@gmail.com
 */
public class SearchQuery extends AbstractObject {

    public static final int DEFAULT_START_PAGE = 1;
    public static final int DEFAULT_ITEMS_PER_PAGE = 2;

    private int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;
    private Set<OrderBy> orderByList;
    private String queryString;
    private int startPage = DEFAULT_START_PAGE;

    /**
     * @param itemsPerPage
     * @param orderByList
     * @param queryString
     * @param startPage
     */
    public SearchQuery() {
        super();
        itemsPerPage = DEFAULT_ITEMS_PER_PAGE;
        startPage = DEFAULT_START_PAGE;
        orderByList = new HashSet<OrderBy>();
        orderByList.add(OrderBy.DEFAULT);
    }

    /**
     * @param queryString
     */
    public SearchQuery(final String queryString) {
        this();
        this.queryString = queryString;
    }

    /**
     * @param queryString
     * @param orderByList
     */
    public SearchQuery(final String queryString, final Set<OrderBy> orderByList) {
        this();
        this.orderByList = orderByList;
        this.queryString = queryString;
    }

    /**
     * @param itemsPerPage
     * @param orderByList
     * @param queryString
     * @param startPage
     */
    public SearchQuery(final String queryString, final Set<OrderBy> orderByList, final int startPage,
            final int itemsPerPage) {
        this();
        this.queryString = queryString;
        this.orderByList = orderByList;
        this.startPage = startPage;
        this.itemsPerPage = itemsPerPage;
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
