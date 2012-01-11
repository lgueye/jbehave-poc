/**
 * 
 */
package org.diveintojee.poc.jbehave.persistence.events;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.springframework.context.ApplicationEvent;

/**
 * @author louis.gueye@gmail.com
 */
public class AdvertPersistenceEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3716026229555243002L;

	/**
	 * @param source
	 */
	public AdvertPersistenceEvent(Advert source) {
		super(source);
	}

	/**
	 * @see java.util.EventObject#getSource()
	 */
	@Override
	public Advert getSource() {
		return (Advert) this.source;
	}

}
