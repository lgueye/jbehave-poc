/*
 *
 */
package org.diveintojee.poc.jbehave.web;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.diveintojee.poc.jbehave.domain.Constants;
import org.diveintojee.poc.jbehave.domain.ResponseError;
import org.diveintojee.poc.jbehave.domain.exceptions.BusinessException;
import org.diveintojee.poc.jbehave.domain.exceptions.LocalizedException;
import org.diveintojee.poc.jbehave.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author louis.gueye@gmail.com
 */
@Component(ExceptionConverter.BEAN_ID)
public class ExceptionConverter {

    public static final String BEAN_ID = "ExceptionConverter";

    public static final List<String> SUPPORTED_MEDIA_TYPES = Arrays.asList(MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML);

    public static final String DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON;

    @Autowired
    private LocaleResolver localeResolver;

    private Locale getLocale(final HttpServletRequest request) {
        final Locale locale = localeResolver.resolveLocale(request);
        if (locale == null)
            return Locale.ENGLISH;
        if (!Constants.SUPPORTED_LOCALES.contains(locale.getLanguage()))
            return Locale.ENGLISH;
        return new Locale(locale.getLanguage());
    }

    /**
     * @param th
     * @return
     */
    public int resolveHttpStatus(final Throwable th) {
        if (th == null)
            return HttpServletResponse.SC_OK;
        // th.printStackTrace();
        if (th instanceof NotFoundException)
            return HttpServletResponse.SC_NOT_FOUND;
        if (th instanceof AuthenticationException)
            return HttpServletResponse.SC_UNAUTHORIZED;
        if (th instanceof AccessDeniedException)
            return HttpServletResponse.SC_FORBIDDEN;
        if (th instanceof IllegalArgumentException || th instanceof ValidationException
            || th instanceof BusinessException)
            return HttpServletResponse.SC_BAD_REQUEST;
        if (th instanceof IllegalStateException)
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        if (th instanceof WebApplicationException && ((WebApplicationException) th).getResponse() != null)
            return ((WebApplicationException) th).getResponse().getStatus();
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    /**
     * @param request
     * @param errorCode
     * @param th
     * @return
     */
    public String resolveMesage(final HttpServletRequest request, final Throwable th) {

        if (th == null && request == null)
            return StringUtils.EMPTY;
        if (th == null)
            return StringUtils.EMPTY;
        if (request == null)
            return th.getMessage();
        if (th instanceof AuthenticationException)
            return ResourceBundle.getBundle("messages", getLocale(request)).getString(
                String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        if (th instanceof AccessDeniedException)
            return ResourceBundle.getBundle("messages", getLocale(request)).getString(
                String.valueOf(HttpServletResponse.SC_FORBIDDEN));
        if (!(th instanceof LocalizedException))
            return th.getMessage();
        return ((LocalizedException) th).getMessage(getLocale(request).getLanguage());

    }

    public ResponseError toResponseError(final Throwable th, final HttpServletRequest request) {
        final String message = resolveMesage(request, th);
        final int httpStatus = resolveHttpStatus(th);
        final ResponseError responseError = new ResponseError(message, httpStatus);
        return responseError;
    }
}
