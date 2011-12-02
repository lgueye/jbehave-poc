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
import fr.explorimmo.poc.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class ErrorHandling {

    @Autowired
    @Qualifier("baseEndPoint")
    String baseEndPoint;
    String responseContentType;
    List<String> resources = new ArrayList<String>();
    private int responseStatus;
    String lastCreatedResourceURI;
    private String responseMessage;
    private String responseLanguage = "en";

    @Then("the response message should be <message>")
    public void expectResponseMessage(@Named("message") final String responseMessage) {
        Assert.assertEquals(responseMessage, this.responseMessage);
    }

    @Then("the response code should be $statusCode")
    public void expectStatusCode(@Named("statusCode") final int statusCode) {
        Assert.assertEquals(statusCode, responseStatus);
    }

    @Then("I should get my newly created resource")
    public void getResourceAtLocation() {
        final URI uri = URI.create(lastCreatedResourceURI);
        final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
        config.getClasses().add(JacksonJsonProvider.class);
        final Client jerseyClient = ApacheHttpClient4.create(config);
        final WebResource webResource = jerseyClient.resource(uri);
        final Advert advert = webResource.header("Accept", "application/json").get(Advert.class);
        Assert.assertNotNull(advert);
    }

    @Then("I should get a successful response")
    public void getResponse() {
        Assert.assertNotNull(responseStatus);
        final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(responseStatus).substring(0, 1));
        Assert.assertTrue(statusCodeFirstDigit == 2);
    }

    @Then("I should get an unsuccessful response")
    public void responseShouldBeUnsuccessful() {
        Assert.assertNotNull(responseStatus);
        final int statusCodeFirstDigit = Integer.valueOf(String.valueOf(responseStatus).substring(0, 1));
        Assert.assertTrue(statusCodeFirstDigit != 2 && statusCodeFirstDigit != 3);
    }

    @When("I send a find advert by reference with reference <reference>.")
    public void sendFindByReferenceRequest(@Named("reference") final String reference) {
        final String path = "/advert/find/reference/" + reference;
        final URI uri = URI.create(baseEndPoint + path);
        final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
        config.getClasses().add(JacksonJsonProvider.class);
        final Client jerseyClient = ApacheHttpClient4.create(config);
        jerseyClient.addFilter(new LoggingFilter());
        final ClientResponse response = jerseyClient.resource(uri).accept(MediaType.valueOf(responseContentType))
                .acceptLanguage(new String[] { responseLanguage }).get(ClientResponse.class);
        responseStatus = response.getStatus();
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

        resources.clear();

        for (int i = 0; i < 10; i++) {

            final Advert advert = TestUtils.validAdvert();
            final String path = "/advert";
            final URI uri = URI.create(baseEndPoint + path);
            final String requestContentType = "application/json";
            final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
            final Client jerseyClient = ApacheHttpClient4.create(config);
            final WebResource webResource = jerseyClient.resource(uri);
            final ClientResponse response = webResource.header("Content-Type", requestContentType).post(
                ClientResponse.class, advert);

            if (response.getLocation() != null) {
                resources.add(response.getLocation().toString());
            }

        }

    }

    @AfterStory
    public void tearDown() {
        for (final String resource : resources) {
            final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
            final Client jerseyClient = ApacheHttpClient4.create(config);
            final WebResource webResource = jerseyClient.resource(resource);
            webResource.delete();
        }
    }

}
