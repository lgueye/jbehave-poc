/**
 *
 */
package org.diveintojee.poc.jbehave.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PlainTextBasicAuthenticationEntryPoint.BEAN_ID)
public class PlainTextBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    public static final String BEAN_ID = "defaultEntryPoint";

    @Autowired
    private ExceptionConverter exceptionConverter;

    public PlainTextBasicAuthenticationEntryPoint() {
        setRealmName("diveintojee.org");
    }

    /**
     * @see org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(exceptionConverter.resolveHttpStatus(authException));
        final PrintWriter writer = response.getWriter();
        final String i18nMessage = exceptionConverter.resolveMesage(request, authException);
        writer.println(i18nMessage);
    }

}
