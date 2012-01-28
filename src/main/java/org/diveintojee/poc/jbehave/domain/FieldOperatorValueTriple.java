/**
 *
 */
package org.diveintojee.poc.jbehave.domain;

import org.apache.commons.lang3.StringUtils;
import org.diveintojee.poc.jbehave.persistence.SearchOperator;

/**
 * @author louis.gueye@gmail.com
 */
public class FieldOperatorValueTriple extends AbstractObject {

	private SearchOperator	operator;
	private String			value;
	private String			field;

	/**
	 * @return the operator
	 */
	public SearchOperator getOperator() {
		return this.operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(SearchOperator operator) {
		this.operator = operator;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return this.field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @param operator
	 * @param value
	 * @param field
	 */
	public FieldOperatorValueTriple(String field, SearchOperator operator, String value) {
		super();
		this.operator = operator;
		this.value = value;
		this.field = field;
	}

	/**
	 * @param clause
	 * @return
	 */
	public static FieldOperatorValueTriple fromClause(String clause) {

		if (StringUtils.isEmpty(clause)) return new FieldOperatorValueTriple(null, null, null);

		int exactTermOperatorIndex = clause.indexOf(SearchOperator.EXACT_MATCH_OPERATOR.toString());

		int fullTextOperatorIndex = clause.indexOf(SearchOperator.FULL_TEXT_OPERATOR.toString());

		if (exactTermOperatorIndex < 0 && fullTextOperatorIndex < 0) return new FieldOperatorValueTriple(null, null,
				clause);

		if (exactTermOperatorIndex < 0) {
			String field = clause.substring(0, fullTextOperatorIndex);
			String value = clause.substring(fullTextOperatorIndex
					+ SearchOperator.FULL_TEXT_OPERATOR.toString().length(), clause.length());
			return new FieldOperatorValueTriple(field, SearchOperator.FULL_TEXT_OPERATOR, value);
		}

		if (fullTextOperatorIndex < 0) {
			String field = clause.substring(0, exactTermOperatorIndex);
			String value = clause.substring(exactTermOperatorIndex
					+ SearchOperator.EXACT_MATCH_OPERATOR.toString().length(), clause.length());
			return new FieldOperatorValueTriple(field, SearchOperator.EXACT_MATCH_OPERATOR, value);
		}

		throw new IllegalStateException();

	}

	/**
	 * @return
	 */
	public boolean isValueOnly() {
		return this.operator == null && StringUtils.isEmpty(this.field) && StringUtils.isNotEmpty(this.value);
	}

}
