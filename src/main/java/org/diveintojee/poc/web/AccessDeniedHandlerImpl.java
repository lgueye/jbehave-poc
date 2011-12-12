/*
 *
 */
package org.diveintojee.poc.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(AccessDeniedHandlerImpl.BEAN_ID)
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    public static final String BEAN_ID = "defaultAccessDeniedHandler";

    @Autowired
    private ExceptionConverter exceptionConverter;

    /**
     * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, org.springframework.security.access.AccessDeniedException)
     */
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
            final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendError(exceptionConverter.resolveHttpStatus(accessDeniedException),
            exceptionConverter.resolveMesage(request, accessDeniedException));
    }

}
