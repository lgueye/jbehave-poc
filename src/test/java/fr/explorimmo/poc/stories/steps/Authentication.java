/*
 *
 */
package fr.explorimmo.poc.stories.steps;

import java.net.URI;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
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
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class Authentication {

	@Autowired
	@Qualifier("baseEndPoint")
	String			baseEndPoint;
	URI				uri;
	Advert			body;
	String			requestContentType;
	ClientConfig	cc;
	Client			jerseyClient;
	ClientResponse	response;

	public Authentication() {

		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		this.jerseyClient = ApacheHttpClient4.create(config);
		this.jerseyClient.addFilter(new LoggingFilter());
		config.getClasses().add(JacksonJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

	}

	@Given("I authenticate with <uid> uid and $password password")
	public void authenticate(@Named("uid") final String uid, @Named("password") final String password) {
		this.jerseyClient.addFilter(new HTTPBasicAuthFilter(uid, password));
	}

	@Given("I send <request-contenttype>")
	public void provideRequestContentType(@Named("request-contenttype") final String requestContentType) {
		this.requestContentType = requestContentType;
	}

	@Given("I provide a valid create food specialty request body")
	public void provideValidRequestBody() {
		this.body = TestUtils.validAdvert();
	}

	@When("I send a create food specialty request")
	public void sendRequest() {
		this.uri = URI.create(this.baseEndPoint + "/foodspecialty");
		final WebResource webResource = this.jerseyClient.resource(this.uri);
		this.response = webResource.header("Content-Type", this.requestContentType).post(ClientResponse.class,
				this.body);
	}

	@Then("I should get an unsuccessfull response")
	public void responseShouldBeUnsuccessfull() {
		Assert.assertNotNull(this.response);
		Assert.assertNotNull(this.response.getStatus());
		final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(this.response.getStatus()).substring(0, 1));
		Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
	}

	@Then("the error message should be <message>")
	public void expectMessage(@Named("message") final String message) {
		Assert.assertEquals(message, this.response.getEntity(String.class).trim());
	}

	@Then("the response code should be $statusCode")
	public void expectStatusCode(@Named("statusCode") final int statusCode) {
		Assert.assertEquals(statusCode, this.response.getStatus());
	}

}
