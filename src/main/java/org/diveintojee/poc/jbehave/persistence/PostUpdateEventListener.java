/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.hibernate.event.PostUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class PostUpdateEventListener implements org.hibernate.event.PostUpdateEventListener {

	/**  */
	private static final long	serialVersionUID	= 1L;

	@Autowired
	private SearchEngine		searchEngine;

	@Override
	public void onPostUpdate(final PostUpdateEvent event) {

		if (event == null) return;

		final Object entity = event.getEntity();

		if (entity == null || !(entity instanceof Advert) || ((Advert) entity).getId() == null) return;

		this.searchEngine.index(Advert.class, entity);

	}

}
