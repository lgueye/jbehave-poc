/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.hibernate.event.PostDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class PostDeleteEventListener implements org.hibernate.event.PostDeleteEventListener {

	/**  */
	private static final long	serialVersionUID	= 1L;

	@Autowired
	private SearchEngine		searchEngine;

	/**
	 * @see org.hibernate.event.PostDeleteEventListener#onPostDelete(org.hibernate.event.PostDeleteEvent)
	 */

	@Override
	public void onPostDelete(final PostDeleteEvent event) {

		if (event == null) return;

		final Object entity = event.getEntity();

		if (entity == null || !(entity instanceof Advert) || ((Advert) entity).getId() == null) return;

		this.searchEngine.removeFromIndex(Advert.class, ((Advert) entity).getId());

	}

}
