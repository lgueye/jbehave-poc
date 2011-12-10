/**
 * 
 */
package org.diveintojee.poc.web;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.diveintojee.poc.domain.Constants;
import org.diveintojee.poc.domain.ResponseError;
import org.diveintojee.poc.domain.exceptions.BusinessException;
import org.diveintojee.poc.domain.exceptions.LocalizedException;
import org.diveintojee.poc.domain.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;


/**
 * @author louis.gueye@gmail.com
 */
@Component
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger			LOGGER					= LoggerFactory.getLogger(GenericExceptionMapper.class);

	@Autowired
	private LocaleResolver				localeResolver;

	@Context
	private HttpServletRequest			request;

	public static final List<String>	SUPPORTED_MEDIA_TYPES	= Arrays.asList(MediaType.APPLICATION_JSON,
																		MediaType.APPLICATION_XML);

	private static final String			DEFAULT_MEDIA_TYPE		= MediaType.APPLICATION_JSON;

	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(Throwable th) {
		final ResponseError error = resolve(th, this.request);
		String preferredResponseMediaType = this.request.getHeader("Accept");

		if (StringUtils.isNotEmpty(preferredResponseMediaType)
				&& !GenericExceptionMapper.SUPPORTED_MEDIA_TYPES.contains(preferredResponseMediaType)) return Response
				.status(error.getHttpStatus()).entity(error)
				.header("Content-Type", GenericExceptionMapper.DEFAULT_MEDIA_TYPE).build();

		return Response.status(error.getHttpStatus()).entity(error).build();
	}

	private Locale getLocale(final HttpServletRequest request) {
		final Locale locale = this.localeResolver.resolveLocale(request);
		if (locale == null) return Locale.ENGLISH;
		if (!Constants.SUPPORTED_LOCALES.contains(locale.getLanguage())) return Locale.ENGLISH;
		return new Locale(locale.getLanguage());
	}

	public ResponseError resolve(final Throwable th, final HttpServletRequest request) {
		final String message = resolveMesage(request, th);
		final int httpStatus = resolveHttpStatus(th);
		final ResponseError responseError = new ResponseError(message, httpStatus);
		GenericExceptionMapper.LOGGER.debug("Resolved response error = " + responseError);
		return responseError;
	}

	/**
	 * @param th
	 * @return
	 */
	int resolveHttpStatus(final Throwable th) {
		if (th == null) return HttpStatus.OK.value();
		// th.printStackTrace();
		if (th instanceof NotFoundException) return HttpStatus.NOT_FOUND.value();
		if (th instanceof IllegalArgumentException || th instanceof ValidationException
				|| th instanceof BusinessException) return HttpStatus.BAD_REQUEST.value();
		if (th instanceof IllegalStateException) return HttpStatus.INTERNAL_SERVER_ERROR.value();
		if (th instanceof WebApplicationException && ((WebApplicationException) th).getResponse() != null) return ((WebApplicationException) th)
				.getResponse().getStatus();
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

	/**
	 * @param request
	 * @param errorCode
	 * @param th
	 * @return
	 */
	String resolveMesage(final HttpServletRequest request, final Throwable th) {

		if (th == null && request == null) return StringUtils.EMPTY;
		if (th == null) return StringUtils.EMPTY;
		if (request == null) return th.getMessage();
		if (!(th instanceof LocalizedException)) return th.getMessage();
		return ((LocalizedException) th).getMessage(getLocale(request).getLanguage());

	}

}
