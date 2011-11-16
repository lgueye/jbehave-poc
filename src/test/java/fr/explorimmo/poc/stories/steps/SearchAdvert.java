/*
 *
 */
package fr.explorimmo.poc.stories.steps;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
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
    ClientConfig cc;
    String responseContentType;
    ClientResponse response;
    List<String> resources = new ArrayList<String>();

    List<Advert> results;

    @Then("I should get $countAdverts adverts")
    public void expectedResult(@Named("countAdverts") final int countAdverts) {
        Assert.assertEquals(countAdverts, results.size());
    }

    @When("I search adverts by criteria: $criteria")
    public void sendRequestWithCriteria(@Named("criteria") final ExamplesTable criteriaAsTable) {
        final StringBuilder queryBuilder = new StringBuilder();
        final Map<String, String> row = criteriaAsTable.getRows().get(0);// Table first and unique row
        for (final Entry<String, String> entry : row.entrySet()) {
            queryBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        final String path = "/advert/find";
        final String query = queryBuilder.toString();
        final URI uri = URI.create(baseEndPoint + path);
        final String requestContentType = "application/x-www-form-urlencoded";
        final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
        config.getClasses().add(JacksonJsonProvider.class);
        final Client jerseyClient = ApacheHttpClient4.create(config);
        // jerseyClient.addFilter(new LoggingFilter());
        final WebResource webResource = jerseyClient.resource(uri);
        results = webResource.accept(MediaType.valueOf(responseContentType)).header("Content-Type", requestContentType)
                .post(new GenericType<List<Advert>>() {}, query);
    }

    @When("I receive <responseContentType>")
    public void setResponseContentType(@Named("responseContentType") final String responseContentType) {
        this.responseContentType = responseContentType;
    }

    @Given("before: $adverts")
    public void setup(@Named("adverts") final ExamplesTable advertsAsTable) {
        for (final Map<String, String> row : advertsAsTable.getRows()) {
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
    }

    @Then("teardown")
    public void tearDown() {
        for (final String resource : resources) {
            final DefaultClientConfig config = new DefaultApacheHttpClient4Config();
            final Client jerseyClient = ApacheHttpClient4.create(config);
            final WebResource webResource = jerseyClient.resource(resource);
            webResource.delete();
        }
        resources.clear();
    }

};
