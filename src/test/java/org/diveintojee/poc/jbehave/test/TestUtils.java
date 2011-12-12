/*
 *
 */
package org.diveintojee.poc.jbehave.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.diveintojee.poc.jbehave.domain.Address;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.junit.Assert;


/**
 * @author louis.gueye@gmail.com
 */
public abstract class TestUtils {

    private static final String charSet = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBNéèçàù7894561230";

    /**
     * @param e
     * @param errorCode
     * @param propertyPath
     */
    public static void assertViolationContainsTemplateAndPath(final ConstraintViolationException e,
            final String errorCode, final String propertyPath) {
        Assert.assertNotNull(e.getConstraintViolations());
        Assert.assertEquals(1, CollectionUtils.size(e.getConstraintViolations()));
        final ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
        Assert.assertEquals(errorCode, violation.getMessageTemplate());
        Assert.assertEquals(propertyPath, violation.getPropertyPath().toString());
    }

    public static <T> T fromJson(final String json, final Class<T> clazz) throws JsonParseException,
            JsonMappingException, IOException {

        final ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json.getBytes(), clazz);

    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXml(final String xml, final Class<T> clazz) throws JAXBException,
            UnsupportedEncodingException {

        final JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { clazz });

        final Unmarshaller xmlUnmarshaller = jaxbContext.createUnmarshaller();

        return (T) xmlUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes("UTF-8")));

    }

    public static <T> String toJson(final T object) throws JsonGenerationException, JsonMappingException, IOException {

        final ObjectMapper mapper = new ObjectMapper();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        mapper.writeValue(out, object);

        return out.toString();

    }

    public static <T> String toXml(final T object) throws JAXBException, UnsupportedEncodingException {

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { object.getClass() });

        final Marshaller xmlMarshaller = jaxbContext.createMarshaller();

        xmlMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        xmlMarshaller.marshal(object, out);

        return out.toString("UTF-8");

    }

    /**
     * @return
     */
    public static Address validAddress() {
        final Address address = new Address();
        address.setCity(RandomStringUtils.random(Address.CONSTRAINT_CITY_MAX_SIZE, TestUtils.charSet));
        address.setCountryCode("fr");
        address.setPostalCode(RandomStringUtils.random(Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE, TestUtils.charSet));
        address.setStreetAddress(RandomStringUtils
                .random(Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE, TestUtils.charSet));
        return address;
    }

    public static Advert validAdvert() {
        final Advert advert = new Advert();
        advert.setAddress(TestUtils.validAddress());
        advert.setDescription(RandomStringUtils.random(Advert.CONSTRAINT_DESCRIPTION_MAX_SIZE, TestUtils.charSet));
        advert.setEmail("foo@bar.com");
        advert.setName(RandomStringUtils.random(Advert.CONSTRAINT_NAME_MAX_SIZE, TestUtils.charSet));
        advert.setPhoneNumber(RandomStringUtils.random(Advert.CONSTRAINT_PHONE_NUMBER_MAX_SIZE, TestUtils.charSet));
        advert.setReference(RandomStringUtils.random(Advert.CONSTRAINT_REFERENCE_MAX_SIZE, TestUtils.charSet));
        return advert;
    }

}
