/**
 *
 */
package org.diveintojee.poc.jbehave.persistence;

/**
 * @author louis.gueye@gmail.com
 */
public enum SearchOperator {
	FULL_TEXT_OPERATOR {
		@Override
		public String toString() {
			return "~";
		}
	},
	EXACT_MATCH_OPERATOR {
		@Override
		public String toString() {
			return ":";
		}
	}
}
