/**
 *
 */
package org.diveintojee.poc.jbehave.domain;

import org.diveintojee.poc.jbehave.persistence.SearchOperator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author louis.gueye@gmail.com
 */
public class FieldOperatorValueTripleTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void isValueOnlyShouldReturnTrue() {
		Assert.assertTrue(new FieldOperatorValueTriple(null, null, "sdsdsdsd").isValueOnly());
		Assert.assertTrue(new FieldOperatorValueTriple("", null, "sdsdsdsd").isValueOnly());
	}

	@Test
	public void isFieldOnlyShouldReturnFalse() {
		Assert.assertFalse(new FieldOperatorValueTriple(null, null, null).isValueOnly());
		Assert.assertFalse(new FieldOperatorValueTriple(null, null, "").isValueOnly());
		Assert.assertFalse(new FieldOperatorValueTriple(null, SearchOperator.EXACT_MATCH_OPERATOR, "xcvxcv")
				.isValueOnly());
		Assert.assertFalse(new FieldOperatorValueTriple("xcvxcv", null, "xcvxcvxcv").isValueOnly());
	}

	@Test
	public void fromClauseShouldReturn3Nullvalues() {
		FieldOperatorValueTriple result;
		String clause;

		// Given
		clause = null;

		// When
		result = FieldOperatorValueTriple.fromClause(clause);

		// Then
		Assert.assertNull(result.getField());
		Assert.assertNull(result.getValue());
		Assert.assertNull(result.getOperator());

		// Given
		clause = "";

		// When
		result = FieldOperatorValueTriple.fromClause(clause);

		// Then
		Assert.assertNull(result.getField());
		Assert.assertNull(result.getValue());
		Assert.assertNull(result.getOperator());
	}

	@Test
	public void fromClauseShouldExtractExactMatchTriple() {

		FieldOperatorValueTriple result;
		String clause;

		// Given
		clause = "field:value";

		// When
		result = FieldOperatorValueTriple.fromClause(clause);

		// Then
		Assert.assertEquals("field", result.getField());
		Assert.assertEquals(SearchOperator.EXACT_MATCH_OPERATOR, result.getOperator());
		Assert.assertEquals("value", result.getValue());

	}

	@Test
	public void fromClauseShouldExtractFullTextTriple() {

		FieldOperatorValueTriple result;
		String clause;

		// Given
		clause = "field~value";

		// When
		result = FieldOperatorValueTriple.fromClause(clause);

		// Then
		Assert.assertEquals("field", result.getField());
		Assert.assertEquals(SearchOperator.FULL_TEXT_OPERATOR, result.getOperator());
		Assert.assertEquals("value", result.getValue());

	}

}
