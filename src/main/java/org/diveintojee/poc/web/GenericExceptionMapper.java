/**
 * 
 */
package org.diveintojee.poc.web;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.diveintojee.poc.domain.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private HttpServletRequest request;

    @Autowired
    private ExceptionConverter exceptionConverter;

    /**
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(final Throwable th) {
        final ResponseError error = exceptionConverter.toResponseError(th, request);
        final String preferredResponseMediaType = request.getHeader("Accept");

        if (StringUtils.isNotEmpty(preferredResponseMediaType)
            && !ExceptionConverter.SUPPORTED_MEDIA_TYPES.contains(preferredResponseMediaType))
            return Response.status(error.getHttpStatus()).entity(error)
                    .header("Content-Type", ExceptionConverter.DEFAULT_MEDIA_TYPE).build();

        return Response.status(error.getHttpStatus()).entity(error).build();
    }

}
