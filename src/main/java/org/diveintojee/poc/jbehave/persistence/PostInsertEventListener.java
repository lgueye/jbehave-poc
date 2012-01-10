/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.hibernate.event.PostInsertEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class PostInsertEventListener implements org.hibernate.event.PostInsertEventListener {

    /**  */
    private static final long serialVersionUID = 1L;

    @Autowired
    private SearchEngine searchEngine;

    /**
     * @see org.hibernate.event.PostInsertEventListener#onPostInsert(org.hibernate.event.PostInsertEvent)
     */

    @Override
    public void onPostInsert(final PostInsertEvent event) {

        if (event == null)
            return;

        final Object entity = event.getEntity();

        if (!(entity instanceof Advert) || ((Advert) entity).getId() == null)
            return;

        searchEngine.index(Advert.class, entity);

    }

}
