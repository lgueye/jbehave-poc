/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import java.io.IOException;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(JsonByteArrayToAdvertConverter.BEAN_ID)
public class JsonByteArrayToAdvertConverter implements Converter<byte[], Advert> {

	public static final String	BEAN_ID	= "jsonByteArrayToAdvertConverter";

	@Autowired
	private ObjectMapper		jsonMapper;

	/**
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */

	@Override
	public Advert convert(final byte[] source) {
		try {
			this.jsonMapper.getDeserializationConfig()
					.without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
			// System.out.write(source);
			final Advert advert = this.jsonMapper.readValue(source, Advert.class);
			return advert;
		} catch (final IOException ignored) {
			throw new IllegalStateException(ignored);
		}
	}

}
