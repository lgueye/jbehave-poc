/**
 * 
 */
package org.diveintojee.poc.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.diveintojee.poc.domain.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PlainTextBasicAuthenticationEntryPoint.BEAN_ID)
public class PlainTextBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	public static final String	BEAN_ID	= "defaultEntryPoint";

	public PlainTextBasicAuthenticationEntryPoint() {
		setRealmName("midipascher.fr");
	}

	@Autowired
	private LocaleResolver	localeResolver;

	/**
	 * @see org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse,
	 *      org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		final PrintWriter writer = response.getWriter();
		final String i18nMessage = ResourceBundle.getBundle("messages", getLocale(request)).getString("401");
		writer.println(i18nMessage);
	}

	private Locale getLocale(final HttpServletRequest request) {
		final Locale locale = this.localeResolver.resolveLocale(request);
		if (locale == null) return Locale.ENGLISH;
		if (!Constants.SUPPORTED_LOCALES.contains(locale.getLanguage())) return Locale.ENGLISH;
		return new Locale(locale.getLanguage());
	}
}
