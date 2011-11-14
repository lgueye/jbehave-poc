/*
 *
 */
package fr.explorimmo.poc.stories.steps;

import java.net.URI;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

import fr.explorimmo.poc.domain.Advert;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class SearchAdvert {

    @Autowired
    @Qualifier("baseEndPoint")
    String baseEndPoint;
    URI uri;
    Advert body;
    String requestContentType;
    ClientConfig cc;
    Client jerseyClient;
    ClientResponse response;

    public SearchAdvert() {

        final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
        jerseyClient = ApacheHttpClient4.create(config);
        jerseyClient.addFilter(new LoggingFilter());
    }

    @Given("adverts: $adverts")
    public void authenticate(@Named("adverts") final ExamplesTable advertsAsTable) {

    }

    @Then("Then I sould get the adverts: adverts")
    public void expectedResult(@Named("adverts") final ExamplesTable advertsAsTable) {
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getStatus());
        final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(response.getStatus()).substring(0, 1));
        Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
    }

    @When("I search adverts by criteria: $criteria")
    public void sendRequestWithCriteria(@Named("criteria") final ExamplesTable criteriaAsTable) {
        uri = URI.create(baseEndPoint + "/advert/find");
        final WebResource webResource = jerseyClient.resource(uri);
        response = webResource.header("Content-Type", requestContentType).post(ClientResponse.class, body);
    }

}
