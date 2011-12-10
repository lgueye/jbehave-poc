/**
 * 
 */
package org.diveintojee.poc.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author louis.gueye@gmail.com
 */
@XmlRootElement
public class ResponseError extends AbstractObject {

	private String	message;
	private int		httpStatus;

	public ResponseError() {

	}

	/**
	 * @param errorCode
	 * @param message
	 * @param httpStatus
	 */
	public ResponseError(String message, int httpStatus) {
		setMessage(message);
		setHttpStatus(httpStatus);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	private void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the httpStatus
	 */
	public int getHttpStatus() {
		return this.httpStatus;
	}

	/**
	 * @param httpStatus
	 *            the httpStatus to set
	 */
	private void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

}
