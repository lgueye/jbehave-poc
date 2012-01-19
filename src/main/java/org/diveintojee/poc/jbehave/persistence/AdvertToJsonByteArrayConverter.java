/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(AdvertToJsonByteArrayConverter.BEAN_ID)
public class AdvertToJsonByteArrayConverter implements Converter<Advert, byte[]> {

    public static final String BEAN_ID = "advertToJsonByteArrayConverter";

    @Autowired
    private ObjectMapper jsonMapper;

    /**
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public byte[] convert(final Advert source) {

        if (source == null)
            return null;

        jsonMapper.getSerializationConfig().without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);

        String string;
        try {
            string = jsonMapper.writeValueAsString(source);
            return string.getBytes("utf-8");
        } catch (final Throwable th) {
            throw new IllegalArgumentException(th);
        }
        // System.out.println("source as string = " + string);
    }

}
