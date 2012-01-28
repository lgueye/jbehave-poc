/**
 *
 */
package org.diveintojee.poc.jbehave.web;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.collections.CollectionUtils;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.OrderBy;
import org.diveintojee.poc.jbehave.domain.SearchResult;
import org.diveintojee.poc.jbehave.domain.business.Facade;
import org.diveintojee.poc.jbehave.domain.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path("/")
public class AdvertController {

	@Autowired
	private Facade						facade;

	@Context
	UriInfo								uriInfo;

	@Autowired
	@Qualifier(StringToOrderByConverter.BEAN_ID)
	private Converter<String, OrderBy>	stringToOrderByConverter;

	private static final Logger			LOGGER	= LoggerFactory.getLogger(AdvertController.class);

	@POST
	@Path("/advert")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response create(final Advert advert) throws Throwable {

		final Long id = this.facade.createAdvert(advert);

		final URI uri = this.uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

		return Response.created(uri).build();

	}

	@DELETE
	@Path("/advert/{id}")
	public Response delete(@PathParam(value = "id") final Long id) throws Throwable {

		this.facade.deleteAdvert(id);

		return Response.noContent().build();

	}

	@POST
	@Path(value = "/advert/find")
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

		final List<Advert> results = this.facade.findAdvertsByCriteria(criteria);

		final GenericEntity<List<Advert>> entity = new GenericEntity<List<Advert>>(results) {
		};

		if (CollectionUtils.isEmpty(results)) {
			AdvertController.LOGGER.info("No results found");
		}

		return Response.ok(entity).build();

	}

	@GET
	@Path("/search/adverts")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response findByCriteria(//
			@QueryParam(value = "query") final String query, //
			@QueryParam(value = "sort") final Set<String> sort,//
			@QueryParam(value = "from") final int from, //
			@QueryParam(value = "itemsPerPage") final int itemsPerPage) throws Throwable {//

		final Set<OrderBy> orderByList = fromParams(sort);

		final SearchResult results = this.facade.findAdvertsByCriteria(query, orderByList, from, itemsPerPage);

		return Response.ok(results).build();

	}

	@GET
	@Path("/advert/find/reference/{reference}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response findByReference(@PathParam(value = "reference") final String reference) throws Throwable {

		final Advert criteria = new Advert();
		criteria.setReference(reference);

		final List<Advert> results = this.facade.findAdvertsByCriteria(criteria);

		if (CollectionUtils.isEmpty(results)) throw new NotFoundException(new Object[] { reference });

		return Response.ok(results.get(0)).build();

	}

	/**
	 * @param sort
	 * @return
	 */
	private Set<OrderBy> fromParams(final Set<String> sort) {

		if (CollectionUtils.isEmpty(sort)) return null;

		final Set<OrderBy> orderByList = new HashSet<OrderBy>();

		for ( final String sortClause : sort ) {

			final OrderBy orderBy = this.stringToOrderByConverter.convert(sortClause);

			orderByList.add(orderBy);

		}

		return orderByList;

	}

	@GET
	@Path("/advert/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response get(@PathParam(value = "id") final Long id) throws Throwable {

		final Advert advert = this.facade.readAdvert(id);

		if (advert == null) return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok(advert).build();

	}

	@GET
	@Path("/advert/protected")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response returnProtectedResource() throws Throwable {

		final Advert criteria = new Advert();

		// very important phone number !!!!!
		criteria.setPhoneNumber("0033606060606");

		final List<Advert> results = this.facade.findProtectedAdvertsByCriteria(criteria);

		final GenericEntity<List<Advert>> entity = new GenericEntity<List<Advert>>(results) {
		};

		if (CollectionUtils.isEmpty(results)) {
			AdvertController.LOGGER.info("No results found");
		}

		return Response.ok(entity).build();

	}

}
