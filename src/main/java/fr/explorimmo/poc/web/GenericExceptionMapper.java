/**
 * 
 */
package fr.explorimmo.poc.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import fr.explorimmo.poc.domain.Constants;
import fr.explorimmo.poc.domain.ResponseError;
import fr.explorimmo.poc.domain.exceptions.BusinessException;
import fr.explorimmo.poc.domain.exceptions.LocalizedException;
import fr.explorimmo.poc.domain.exceptions.NotFoundException;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(GenericExceptionMapper.class);

	@Autowired
	private LocaleResolver		localeResolver;

	@Context
	private HttpServletRequest	request;

	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(Throwable th) {
		final ResponseError error = resolve(th, this.request);
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
		if (th instanceof NotFoundException) return HttpStatus.NOT_FOUND.value();
		if (th instanceof IllegalArgumentException || th instanceof ValidationException
				|| th instanceof BusinessException) return HttpStatus.BAD_REQUEST.value();
		if (th instanceof IllegalStateException) return HttpStatus.INTERNAL_SERVER_ERROR.value();
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
