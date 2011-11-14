/**
 * 
 */
package fr.explorimmo.poc.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

import fr.explorimmo.poc.domain.Advert;

/**
 * @author louis.gueye@gmail.com
 */
public class TestUtilsTest {

	/**
	 * Test method for
	 * {@link fr.explorimmo.poc.test.TestUtils#fromJson(java.lang.String, java.lang.Class)}
	 * .
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Test
	public final void testFromJson() throws JsonParseException, JsonMappingException, IOException {
		// Given
		String json = "{\"address\":{\"city\":\"4Vf2nIàvOzSlysrLWJxS8YmtD44MyTeyFkàcchùWa46Y1Zk5éo\",\"countryCode\":\"fr\",\"postalCode\":\"qBBOéSZRhW\",\"streetAddress\":\"CmVNYlZ7zéAbHo99OolgawSfvjQd9elBbéF6VCç5R1jHeBdQQE\"},\"name\":\"3jV3TdAyiks5èçmEàt68uSEvYVwXsw1ouDkuxsAB7Kjçéq3F3\",\"id\":null,\"description\":\"LYHèFvTz9mAmNCULL7ieds3E3oc6Lp6qJWM2SYçMsz31Fh5TIPj3LCçuCL7ErG5HG0V0tFN6x5ùAMpRSGzpbrsGZVDK1vrGkàWYTCùSijhWéy09xCndaHJYIzWSvxErJroùçUdPrMUFxlwpPTsBcQZz4uPfV6nàtè9ZxwgNm5mZPkZ1u6ANCdNFoiuYLMq4oXZM\",\"email\":\"foo@bar.com\",\"mainOffer\":\"ph2gYDZltx7GryAhCDDLLmùèSiçX8UNwTçbjWyUVG1éR5YGùJM11ùtàDh2tcUtRDEXHQàRçKqHwvhGJ4k27leinbt2tnXJ2t78iaUBzNRJGpElhNGFFUdMamH2YxTJXkDu3vnéIvenaKi6éNPDQcys0Azèhnro8Ybx0èS2gVo8CDuùBVviJJww5oGJOQQ1aSCz8K7Yv\",\"phoneNumber\":\"KnwIr7w3qiDXPB9cGYçK\",\"specialties\":[{\"id\":null,\"code\":\"fb5çSuYp3è\",\"label\":\"DcVsLfHzTù5saà8Bn1Rto5EBdd3c5ZaDbd9QTy1sw9GP5r6m0m\",\"active\":true}],\"halal\":true,\"kosher\":false}";
		// When
		Advert advert = TestUtils.fromJson(json, Advert.class);

		// Then
		Assert.assertNotNull(advert);
	}

	/**
	 * Test method for
	 * {@link fr.explorimmo.poc.test.TestUtils#fromXml(java.lang.String, java.lang.Class)}
	 * .
	 * 
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public final void testFromXml() throws UnsupportedEncodingException, JAXBException {
		// Given
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<advert>"
				+ "<address>"
				+ "<city>wI3yKp8éuéçpTPQAGlP9Bé0èjàààffgLHSOdèè3tsèLdYDwKlI</city>"
				+ "<countryCode>fr</countryCode>"
				+ "<postalCode>6LoiQrlJIq</postalCode>"
				+ "<streetAddress>NI0Oéaakb65gxvVmlbyQvwCé94oZbSYa9JQAéFUgHùAFxxRIùW</streetAddress>"
				+ "</address>"
				+ "<description>LwZOZ9p5N6DTodNF3N2éXy3SUjxHkFMP6BvlUYlTuKNMREcmORhr2làU0MGwBmUGGv3OlmX0IQroJbBrçmBOX6fnoé7jà9Hèè5nWN6b8qeATyunuJelwwu52IàrgxiRL3Fpkàa4rtCh8aztlcCé9AIfSdLqeTfWBaq2LçV232GàiwOèybçOrLDCLMçç4ydUskE4rI</description>"
				+ "<email>foo@bar.com</email>"
				+ "<halal>true</halal>"
				+ "<kosher>false</kosher>"
				+ "<mainOffer>myneOmScmzOW7RZ1qeFj4mS5wèWc51bf4t8oRZK8Hvw8ùçft7çd8ébCérw3J3xqUIX5rDCkWLLéUH3mègJbl79HjfXù1HtXoà3AbpqooSswOu1pqqmE5ZMyrgHo8VnsM1a5qcdANRTGs5EKxmfNOGlkVoF99IN4JKVyByWKRùsvxaGHD2nL1JL3T5H1xZ5çiKèD98M</mainOffer>"
				+ "<name>DépGsdDnCCpyxwIéJVOùCoàYgHISYEA5l2yS3daz9U3y8n</name>"
				+ "<phoneNumber>mVVhW2FD4mèJEMkùdRàM</phoneNumber>" + "<specialties>" + "<active>true</active>"
				+ "<code>PidKàçedo2</code>" + "<label>VVlSrxY5ALbtXRùèL7W917K58nPèQ8IFmkfJYéyuàVjKrk1d6i</label>"
				+ "</specialties>" + "</advert>";
		// When
		Advert advert = TestUtils.fromXml(xml, Advert.class);

		// Then
		Assert.assertNotNull(advert);
	}

	/**
	 * Test method for
	 * {@link fr.explorimmo.poc.test.TestUtils#toJson(java.lang.Object)}.
	 * 
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	@Test
	public final void testToJson() throws JsonGenerationException, JsonMappingException, IOException {
		// Given
		Advert advert = TestUtils.validAdvert();

		// When
		String json = TestUtils.toJson(advert);
		// System.out.println(json);

		// Then
		Assert.assertNotNull(json);
	}

	/**
	 * Test method for
	 * {@link fr.explorimmo.poc.test.TestUtils#toXml(java.lang.Object)}.
	 * 
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public final void testToXml() throws UnsupportedEncodingException, JAXBException {
		// Given
		Advert advert = TestUtils.validAdvert();
		// When
		String xml = TestUtils.toXml(advert);

		// Then
		// System.out.println(xml);
		Assert.assertNotNull(xml);
	}

}
