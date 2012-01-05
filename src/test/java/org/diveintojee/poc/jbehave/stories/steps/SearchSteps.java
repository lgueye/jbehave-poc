/*
 *
 */
package org.diveintojee.poc.jbehave.stories.steps;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

/**
 * @author louis.gueye@gmail.com
 */
public class SearchSteps {
	private final String		baseEndPoint	= ResourceBundle.getBundle("stories-context").getString("baseEndPoint");
	private final List<String>	resources		= new ArrayList<String>();
	private String				responseContentType;

	// When I find by exact reference "B0035WVABS"
	// And I receive <responseContentType>
	// Then I should get the following adverts:

	// \"$reference\"

	@Then("I should get the following adverts: $adverts")
	public void expectedResults(@Named("adverts") final ExamplesTable advertsAsTable) {

	}

	@When("I find by exact reference \"$reference\"")
	public void findByExactReference(@Named("reference") final String reference) {
		final String query = "q=reference:" + reference;
		final String path = "/search/adverts?" + query;
		final URI uri = URI.create(this.baseEndPoint + path);
		final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
		config.getClasses().add(JacksonJsonProvider.class);
		final Client jerseyClient = ApacheHttpClient4.create(config);
		jerseyClient.addFilter(new LoggingFilter());
		final ClientResponse response = jerseyClient.resource(uri).accept(MediaType.valueOf(this.responseContentType))
				.acceptLanguage(new String[] { "en" }).get(ClientResponse.class);
		response.getEntity(List.class);
	}

	@When("I receive <responseContentType> data")
	public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
		this.responseContentType = responseContentType;
	}

	@Given("adverts: $adverts")
	public void setup(@Named("adverts") final ExamplesTable advertsAsTable) {
		for (final Map<String, String> row : advertsAsTable.getRows())
			if (row != null) {
				final Advert advert = new Advert();
				advert.getAddress().setCity(row.get("address.city"));
				advert.getAddress().setCountryCode("fr");
				advert.getAddress().setPostalCode(row.get("address.postalCode"));
				advert.getAddress().setStreetAddress(row.get("address.streetAddress"));
				advert.setDescription(row.get("description"));
				advert.setEmail(row.get("email"));
				advert.setName(row.get("name"));
				advert.setPhoneNumber(row.get("phoneNumber"));
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
}
