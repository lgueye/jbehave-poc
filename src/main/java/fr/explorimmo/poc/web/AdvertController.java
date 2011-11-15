/**
 * 
 */
package fr.explorimmo.poc.web;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.domain.business.Facade;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(value = "/advert")
@Scope("request")
public class AdvertController {

    @Autowired
    private Facade facade;

    @Context
    UriInfo uriInfo;

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(AdvertController.class);

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response create(final Advert advert) throws Throwable {

        final Long id = facade.createAdvert(advert);

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

        return Response.created(uri).build();

    }

    @POST
    @Path(value = "find")
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response find(final @FormParam("description") String description, final @FormParam("name") String name,
            final @FormParam("address.streetAddress") String streetAddress,
            final @FormParam("address.city") String city, final @FormParam("address.postalCode") String postalCode,
            final @FormParam("address.countryCode") String countryCode) throws Throwable {

        final Advert criteria = new Advert();
        criteria.setName(name);
        criteria.setDescription(description);
        criteria.getAddress().setStreetAddress(streetAddress);
        criteria.getAddress().setCity(city);
        criteria.getAddress().setPostalCode(postalCode);
        criteria.getAddress().setCountryCode(countryCode);

        final List<Advert> results = facade.findAdvertsByCriteria(criteria);
        final GenericEntity<List<Advert>> entity = new GenericEntity<List<Advert>>(results) {};
        return Response.ok(entity).build();

    }
}
