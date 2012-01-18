/*
 *
 */
package org.diveintojee.poc.jbehave.domain;

/**
 * @author louis.gueye@gmail.com
 */
public class OrderBy extends AbstractObject {

	public static final String	DEFAULT_FIELD	= "id";

	public static final OrderBy	DEFAULT			= new OrderBy(DEFAULT_FIELD, SortDirection.DESC);

	private SortDirection		sortDirection;

	private String				field;

	public OrderBy() {
		super();
	}

	/**
	 * @param sortDirection
	 * @param field
	 */
	public OrderBy(final String field, final SortDirection sortDirection) {
		super();
		this.sortDirection = sortDirection;
		this.field = field;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final OrderBy other = (OrderBy) obj;
		if (this.field == null) {
			if (other.field != null) return false;
		} else if (!this.field.equals(other.field)) return false;
		if (this.sortDirection != other.sortDirection) return false;
		return true;
	}

	public String getField() {
		return this.field;
	}

	public SortDirection getSortDirection() {
		return this.sortDirection;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.field == null ? 0 : this.field.hashCode());
		result = prime * result + (this.sortDirection == null ? 0 : this.sortDirection.hashCode());
		return result;
	}

	public void setField(final String field) {
		this.field = field;
	}

	public void setSortDirection(final SortDirection sortDirection) {
		this.sortDirection = sortDirection;
	}

}
