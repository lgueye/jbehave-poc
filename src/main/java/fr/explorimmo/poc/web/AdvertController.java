/**
 * 
 */
package fr.explorimmo.poc.web;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
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
	private Facade	facade;

	@Context
	UriInfo			uriInfo;

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(AdvertController.class);

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response create(final Advert advert) throws Throwable {

		final Long id = this.facade.createAdvert(advert);

		final URI uri = this.uriInfo.getAbsolutePathBuilder().path(String.valueOf(id)).build();

		return Response.created(uri).build();

	}

	//
	// @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	// @ResponseStatus(value = HttpStatus.OK)
	// @ResponseBody
	// public FoodSpecialty get(@PathVariable("id") final Long foodSpecialtyId)
	// {
	// return this.facade.readFoodSpecialty(foodSpecialtyId);
	// }
	//
	// @RequestMapping(method = RequestMethod.GET)
	// @ResponseBody
	// public List<FoodSpecialty> list() {
	// return this.facade.listFoodSpecialties();
	// }
	//
	// @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	// public HttpEntity<String> update(@RequestBody final FoodSpecialty
	// foodSpecialty) throws Throwable {
	//
	// this.facade.updateFoodSpecialty(foodSpecialty);
	//
	// final ResponseEntity<String> responseEntity = new
	// ResponseEntity<String>(HttpStatus.OK);
	//
	// return responseEntity;
	//
	// }
	//
	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	// @ResponseStatus(value = HttpStatus.OK)
	// public HttpEntity<String> delete(@PathVariable("id") final Long
	// foodSpecialtyId) {
	//
	// this.facade.deleteFoodSpecialty(foodSpecialtyId);
	//
	// final ResponseEntity<String> responseEntity = new
	// ResponseEntity<String>(HttpStatus.OK);
	//
	// return responseEntity;
	//
	// }
	//
	// @Autowired
	// private ExceptionResolver exceptionResolver;
	//
	// @Override
	// @ExceptionHandler
	// protected HttpEntity<ResponseError> handleThrowable(HttpServletRequest
	// request, Throwable exception) {
	//
	// ResponseError response = this.exceptionResolver.resolve(exception,
	// request);
	//
	// System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Resolved "
	// + response);
	//
	// HttpHeaders headers = new HttpHeaders();
	//
	// String accept = request.getHeader("Accept");
	//
	// if (StringUtils.isEmpty(accept))
	// headers.setContentType(MediaType.APPLICATION_JSON);
	// else
	// headers.setContentType(MediaType.valueOf(accept));
	//
	// final ResponseEntity<ResponseError> responseEntity = new
	// ResponseEntity<ResponseError>(response, headers,
	// HttpStatus.valueOf(response.getHttpStatus()));
	//
	// return responseEntity;
	// }

}
