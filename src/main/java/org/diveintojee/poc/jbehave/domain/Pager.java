/*
 *
 */
package org.diveintojee.poc.jbehave.domain;

/**
 * @author louis.gueye@gmail.com
 */
public class Pager {

    private static final int DEFAULT_PAGE_INDEX = 0;
    private static final int DEFAULT_ITEMS_PER_PAGE = 1;

    private int first;
    private int previous;
    private int next;
    private int last;
    private int internalPageIndex;
    private int internalItemsPerPage;

    /**
     * @param uri
     * @param totalHits
     * @param itemsPerPage
     * @param startPage
     */
    public Pager(final long totalHits, final int itemsPerPage, final int pageIndex) {
        if (totalHits <= 0)
            return;

        internalPageIndex = Math.max(DEFAULT_PAGE_INDEX, pageIndex);
        internalItemsPerPage = Math.max(DEFAULT_ITEMS_PER_PAGE, itemsPerPage);
        setFirst(0);
        setLast(countPages(totalHits, internalItemsPerPage));
        setPrevious(Math.max(internalPageIndex - 1, getFirst()));
        setNext(Math.min(internalPageIndex + 1, getLast()));

    }

    /**
     * @param totalHits
     * @param itemsPerPage
     * @return
     */
    protected int countPages(final long totalHits, final int itemsPerPage) {
        return totalHits % itemsPerPage == 0 ? Long.valueOf(totalHits / itemsPerPage).intValue() : Long.valueOf(
            totalHits / itemsPerPage).intValue() + 1;
    }

    private int getFirst() {
        return first;
    }

    public int getInternalItemsPerPage() {
        return internalItemsPerPage;
    }

    public int getInternalPageIndex() {
        return internalPageIndex;
    }

    private int getLast() {
        return last;
    }

    private void setFirst(final int first) {
        this.first = first;
    }

    private void setLast(final int last) {
        this.last = last;
    }

    private void setNext(final int next) {
        this.next = next;
    }

    private void setPrevious(final int previous) {
        this.previous = previous;
    }

}
