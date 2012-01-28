/**
 *
 */
package org.diveintojee.poc.jbehave.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.diveintojee.poc.jbehave.domain.OrderBy;
import org.diveintojee.poc.jbehave.domain.SortDirection;
import org.junit.Before;
import org.junit.Test;

/**
 * @author louis.gueye@gmail.com
 */
public class StringToOrderByConverterTest {

	private StringToOrderByConverter	underTest;

	@Before
	public void before() {
		this.underTest = new StringToOrderByConverter();
	}

	/**
	 * Test method for
	 * {@link org.diveintojee.poc.jbehave.web.StringToOrderByConverter#convert(java.lang.String)}
	 * .
	 */
	@Test
	public final void convertShouldReturnNullWithEmptyInput() {

		// Variables
		String source;
		OrderBy result;

		// Given
		source = null;

		// When
		result = this.underTest.convert(source);

		// Then
		assertNull(result);

		// Given
		source = "";

		// When
		result = this.underTest.convert(source);

		// Then
		assertNull(result);

	}

	/**
	 * Test method for
	 * {@link org.diveintojee.poc.jbehave.web.StringToOrderByConverter#convert(java.lang.String)}
	 * .
	 */
	@Test
	public final void convertShouldSucceed() {

		// Variables
		String field;
		String sortDirection;
		String source;
		OrderBy result;
		OrderBy expectedOrderBy;

		// Given
		field = "reference";
		sortDirection = "asc";
		source = field + " " + sortDirection;
		expectedOrderBy = new OrderBy(field, SortDirection.fromString(sortDirection));

		// When
		result = this.underTest.convert(source);

		// Then
		assertEquals(expectedOrderBy, result);

	}
}
