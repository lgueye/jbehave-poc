/**
 * 
 */
package org.diveintojee.poc.domain.exceptions;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.diveintojee.poc.domain.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author louis.gueye@gmail.com
 */
public class BusinessException extends RuntimeException implements LocalizedException {

	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOG					= LoggerFactory.getLogger(BusinessException.class);

	private String				messageCode;
	private Object[]			messageArgs;
	private String				defaultMessage;
	private Throwable			cause;

	public BusinessException(final String message) {
		super(message);
	}

	/**
	 * @param messageCode
	 * @param messageArgs
	 * @param defaultMessage
	 */
	public BusinessException(final String messageCode, final Object[] messageArgs, final String defaultMessage) {
		setMessageCode(messageCode);
		setMessageArgs(messageArgs);
		setDefaultMessage(defaultMessage);
	}

	/**
	 * @param messageCode
	 * @param messageArgs
	 * @param defaultMessage
	 * @param cause
	 */
	public BusinessException(final String messageCode, final Object[] messageArgs, final String defaultMessage,
			final Throwable cause) {
		this(messageCode, messageArgs, defaultMessage);
		setCause(cause);
	}

	/**
	 * @return the cause
	 */
	@Override
	public Throwable getCause() {
		return this.cause;
	}

	/**
	 * @return the defaultMessage
	 */
	public String getDefaultMessage() {
		return this.defaultMessage;
	}

	/**
	 * @see org.diveintojee.poc.domain.exceptions.LocalizedException#getMessage(java.lang.String)
	 */
	@Override
	public String getMessage(String preferredLanguage) {

		if (StringUtils.isEmpty(getMessageCode())) return getDefaultMessage();

		Locale locale = (StringUtils.isEmpty(preferredLanguage)) ? Locale.ENGLISH : new Locale(preferredLanguage);

		ResourceBundle bundle = null;

		try {

			bundle = ResourceBundle.getBundle(Constants.MESSAGES_BUNDLE_NAME, locale);

		} catch (final MissingResourceException e) {

			BusinessException.LOG.debug("Bundle '" + Constants.MESSAGES_BUNDLE_NAME + "' not found for locale '"
					+ locale.getLanguage() + "'. Using default message");

			return getDefaultMessage();

		}

		String message = null;

		try {

			message = bundle.getString(getMessageCode());

		} catch (final MissingResourceException e) {

			BusinessException.LOG.debug("Message not found for key '" + getMessageCode() + "'. Using default message");

			return getDefaultMessage();

		}

		if (!ArrayUtils.isEmpty(getMessageArgs())) message = MessageFormat.format(message, getMessageArgs());

		return message;
	}

	/**
	 * @return the messageArgs
	 */
	public Object[] getMessageArgs() {
		return this.messageArgs;
	}

	/**
	 * @return the messageCode
	 */
	public String getMessageCode() {
		return this.messageCode;
	}

	/**
	 * @param cause
	 *            the cause to set
	 */
	private void setCause(final Throwable cause) {
		this.cause = cause;
	}

	/**
	 * @param defaultMessage
	 *            the defaultMessage to set
	 */
	private void setDefaultMessage(final String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	/**
	 * @param messageArgs
	 *            the messageArgs to set
	 */
	private void setMessageArgs(final Object[] messageArgs) {
		this.messageArgs = messageArgs;
	}

	/**
	 * @param messageCode
	 *            the messageCode to set
	 */
	private void setMessageCode(final String messageCode) {
		this.messageCode = messageCode;
	}
}
