/**
 *
 */
package org.diveintojee.poc.jbehave.persistence.events;

import org.diveintojee.poc.jbehave.domain.Advert;

/**
 * @author louis.gueye@gmail.com
 */
public class PostDeleteAdvertEvent extends AdvertPersistenceEvent {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 3716026229555243002L;

	/**
	 * @param source
	 */
	public PostDeleteAdvertEvent(Advert source) {
		super(source);
	}
}
