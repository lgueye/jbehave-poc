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
public class ClauseTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void isValueOnlyShouldReturnTrue() {
		Assert.assertTrue(new Clause(null, null, "sdsdsdsd").isValueOnly());
		Assert.assertTrue(new Clause("", null, "sdsdsdsd").isValueOnly());
	}

	@Test
	public void isFieldOnlyShouldReturnFalse() {
		Assert.assertFalse(new Clause(null, null, null).isValueOnly());
		Assert.assertFalse(new Clause(null, null, "").isValueOnly());
		Assert.assertFalse(new Clause(null, SearchOperator.EXACT_MATCH_OPERATOR, "xcvxcv")
				.isValueOnly());
		Assert.assertFalse(new Clause("xcvxcv", null, "xcvxcvxcv").isValueOnly());
	}

	@Test
	public void fromClauseShouldReturn3Nullvalues() {
		Clause result;
		String clause;

		// Given
		clause = null;

		// When
		result = Clause.fromClause(clause);

		// Then
		Assert.assertNull(result.getField());
		Assert.assertNull(result.getValue());
		Assert.assertNull(result.getOperator());

		// Given
		clause = "";

		// When
		result = Clause.fromClause(clause);

		// Then
		Assert.assertNull(result.getField());
		Assert.assertNull(result.getValue());
		Assert.assertNull(result.getOperator());
	}

	@Test
	public void fromClauseShouldExtractExactMatchTriple() {

		Clause result;
		String clause;

		// Given
		clause = "field:value";

		// When
		result = Clause.fromClause(clause);

		// Then
		Assert.assertEquals("field", result.getField());
		Assert.assertEquals(SearchOperator.EXACT_MATCH_OPERATOR, result.getOperator());
		Assert.assertEquals("value", result.getValue());

	}

	@Test
	public void fromClauseShouldExtractFullTextTriple() {

		Clause result;
		String clause;

		// Given
		clause = "field~value";

		// When
		result = Clause.fromClause(clause);

		// Then
		Assert.assertEquals("field", result.getField());
		Assert.assertEquals(SearchOperator.FULL_TEXT_OPERATOR, result.getOperator());
		Assert.assertEquals("value", result.getValue());

	}

}
