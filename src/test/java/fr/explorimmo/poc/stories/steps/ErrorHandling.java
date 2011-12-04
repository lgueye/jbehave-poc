/*
 *
 */
package fr.explorimmo.poc.stories.steps;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.domain.ResponseError;
import fr.explorimmo.poc.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class ErrorHandling {

	@Autowired
	@Qualifier("baseEndPoint")
	String					baseEndPoint;
	String					responseContentType;
	List<String>			resources			= new ArrayList<String>();
	String					lastCreatedResourceURI;
	private String			responseLanguage	= "en";
	private ClientResponse	response;

	@Then("the response message should be <message>")
	public void expectResponseMessage(@Named("message") final String responseMessage) {
		ResponseError error = this.response.getEntity(ResponseError.class);
		Assert.assertEquals(responseMessage, error.getMessage());
	}

	@Then("the response code should be $statusCode")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

	@Then("I should get an unsuccessful response")
	public void responseShouldBeUnsuccessful() {
		int responseStatus = this.response.getStatus();
		final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(responseStatus).substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
	}

	@When("I send a find advert by reference with reference <reference>.")
	public void sendFindByReferenceRequest(@Named("reference") final String reference) {
		final String path = "/advert/find/reference/" + reference;
		final URI uri = URI.create(this.baseEndPoint + path);
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		this.response = jerseyClient.resource(uri).accept(MediaType.valueOf(this.responseContentType))
				.acceptLanguage(new String[] { this.responseLanguage }).get(ClientResponse.class);
	}

	@Given("I accept <responseLanguage> language")
	public void setAcceptLanguage(@Named("responseLanguage") final String responseLanguage) {
		this.responseLanguage = responseLanguage;
	}

	@Given("I receive <responseContentType> data")
	public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
		this.responseContentType = responseContentType;
	}

	@BeforeStory
	public void setup() {

		this.resources.clear();

		for (int i = 0; i < 2; i++) {

			final Advert advert = TestUtils.validAdvert();
			final String path = "/advert";
			final URI uri = URI.create(this.baseEndPoint + path);
			final String requestContentType = "application/json";
			final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
			final Client jerseyClient = ApacheHttpClient4.create(config);
			final WebResource webResource = jerseyClient.resource(uri);
			final ClientResponse response = webResource.header("Content-Type", requestContentType).post(
					ClientResponse.class, advert);

			if (response.getLocation() != null) this.resources.add(response.getLocation().toString());

		}

	}

	@AfterStory
	public void tearDown() {
		for (final String resource : this.resources) {
			final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
			final Client jerseyClient = ApacheHttpClient4.create(config);
			final WebResource webResource = jerseyClient.resource(resource);
			webResource.delete();
		}
	}

}
