/**
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class AdvertToJsonByteArrayConverterTest {

	@Mock
	ObjectMapper					jsonMapper;

	@InjectMocks
	AdvertToJsonByteArrayConverter	underTest	= new AdvertToJsonByteArrayConverter();

	/**
	 * Test method for
	 * {@link org.diveintojee.poc.jbehave.persistence.AdvertToJsonByteArrayConverter#convert(org.diveintojee.poc.jbehave.domain.Advert)}
	 * .
	 */
	@Test
	public final void convertShoulReturnNullWithNullInput() {

		// Variables
		Advert source;

		// Given
		source = null;

		// When
		byte[] jsonByteArray = this.underTest.convert(source);

		// Then
		assertNull(jsonByteArray);
	}

	/**
	 * Test method for
	 * {@link org.diveintojee.poc.jbehave.persistence.AdvertToJsonByteArrayConverter#convert(org.diveintojee.poc.jbehave.domain.Advert)}
	 * .
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@Test
	public final void convertShouldSucceed() throws JsonGenerationException, JsonMappingException, IOException {

		// Variables
		Advert source;
		SerializationConfig serializationConfig;
		String jsonString;
		byte[] jsonByteArray;

		// Given
		source = new Advert();
		serializationConfig = Mockito.mock(SerializationConfig.class);
		jsonString = "{}";
		jsonByteArray = jsonString.getBytes("utf-8");

		// When
		Mockito.when(this.jsonMapper.getSerializationConfig()).thenReturn(serializationConfig);
		Mockito.when(serializationConfig.without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS)).thenReturn(
				serializationConfig);
		Mockito.when(this.jsonMapper.writeValueAsString(source)).thenReturn(jsonString);
		byte[] result = this.underTest.convert(source);

		// Then
		Mockito.verify(this.jsonMapper).getSerializationConfig();
		Mockito.verify(serializationConfig).without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
		Mockito.verify(this.jsonMapper).writeValueAsString(source);

		Mockito.verifyNoMoreInteractions(this.jsonMapper, serializationConfig);

		assertTrue(Arrays.equals(jsonByteArray, result));

	}
}
