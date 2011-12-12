/**
 * 
 */
package org.diveintojee.poc.jbehave.domain.exceptions;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import junit.framework.Assert;

import org.diveintojee.poc.jbehave.domain.Constants;
import org.diveintojee.poc.jbehave.domain.exceptions.BusinessException;
import org.junit.Test;


/**
 * @author louis.gueye@gmail.com
 */
public class BusinessExceptionTest {

	/**
	 * Test method for
	 * {@link org.diveintojee.poc.jbehave.domain.exceptions.BusinessException#getMessage()}
	 * .
	 */
	@Test
	public final void testGetMessage() {
		final String messageCode = "test.code";
		final Object[] messageArgs = new Object[] { "sdfsdf", 5L };
		final String defaultMessage = "default message";
		final String preferredLanguage = Locale.FRENCH.getLanguage();
		final Throwable cause = new Throwable("bla bla");
		BusinessException e = null;

		e = new BusinessException(null, messageArgs, defaultMessage, cause);
		Assert.assertEquals(e.getDefaultMessage(), e.getMessage(preferredLanguage));

		final String rawMessage = ResourceBundle.getBundle(Constants.MESSAGES_BUNDLE_NAME).getString(messageCode);
		e = new BusinessException(messageCode, null, defaultMessage, cause);
		Assert.assertEquals(rawMessage, e.getMessage(preferredLanguage));

		e = new BusinessException(messageCode, messageArgs, defaultMessage, cause);
		Assert.assertEquals(MessageFormat.format(rawMessage, messageArgs), e.getMessage(preferredLanguage));
	}

}
