/**
 * 
 */
package org.diveintojee.poc.jbehave.web;

import org.apache.commons.lang3.StringUtils;
import org.diveintojee.poc.jbehave.domain.OrderBy;
import org.diveintojee.poc.jbehave.domain.SortDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(StringToOrderByConverter.BEAN_ID)
public class StringToOrderByConverter implements Converter<String, OrderBy> {

	public static final String	BEAN_ID	= "stringToOrderByConverter";

	/**
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public OrderBy convert(String source) {

		if (StringUtils.isEmpty(source)) return null;

		String[] tokens = source.trim().split(" ");

		String field = tokens[0];

		if (StringUtils.isEmpty(field)) return null;

		if (StringUtils.isEmpty(tokens[1])) return null;

		SortDirection sortDirection = SortDirection.fromString(tokens[1]);

		return new OrderBy(field, sortDirection);

	}
}
