/*
 *
 */
package org.diveintojee.poc.jbehave.domain;


/**
 * @author louis.gueye@gmail.com
 */
public class OrderBy extends AbstractObject {

    public static final String DEFAULT_FIELD = "id";

    private SortDirection sortDirection;

    private String field;

    /**
     * @param sortDirection
     * @param field
     */
    public OrderBy(final SortDirection sortDirection, final String field) {
        super();
        this.sortDirection = sortDirection;
        this.field = field;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OrderBy other = (OrderBy) obj;
        if (field == null) {
            if (other.field != null)
                return false;
        } else if (!field.equals(other.field))
            return false;
        if (sortDirection != other.sortDirection)
            return false;
        return true;
    }

    public String getField() {
        return field;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    /**
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (field == null ? 0 : field.hashCode());
        result = prime * result + (sortDirection == null ? 0 : sortDirection.hashCode());
        return result;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public void setSortDirection(final SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

}
