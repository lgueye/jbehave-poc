/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Address;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.hibernate.event.PostDeleteEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class PostDeleteEventListenerTest {

    @Mock
    private SearchEngine searchEngine;

    @InjectMocks
    private final PostDeleteEventListener underTest = new PostDeleteEventListener();

    @Test
    public void onPostDeleteWillInvokeSearchEngine() {

        // Given
        final PostDeleteEvent event = Mockito.mock(PostDeleteEvent.class);
        final Advert advert = new Advert();
        advert.setId(5L);
        Mockito.when(event.getEntity()).thenReturn(advert);

        // When
        underTest.onPostDelete(event);

        // Then
        Mockito.verify(searchEngine).removeFromIndex(Advert.class, advert.getId());
        Mockito.verifyNoMoreInteractions(searchEngine);

    }

    @Test
    public void onPostDeleteWontInvokeSearchEngineIfEventEntityIsNotAnAdvert() {

        // Given
        final PostDeleteEvent event = Mockito.mock(PostDeleteEvent.class);
        Mockito.when(event.getEntity()).thenReturn(new Address());

        // When
        underTest.onPostDelete(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

    @Test
    public void onPostDeleteWontInvokeSearchEngineWithNullEventEntity() {

        // Given
        final PostDeleteEvent event = Mockito.mock(PostDeleteEvent.class);
        Mockito.when(event.getEntity()).thenReturn(null);

        // When
        underTest.onPostDelete(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

    @Test
    public void onPostDeleteWontInvokeSearchEngineWithNullEventEntityId() {

        // Given
        final PostDeleteEvent event = Mockito.mock(PostDeleteEvent.class);
        final Advert advert = new Advert();
        Mockito.when(event.getEntity()).thenReturn(advert);

        // When
        underTest.onPostDelete(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

    @Test
    public void onPostDeleteWontInvokeSearchEngineWithNullInput() {

        // Given
        final PostDeleteEvent event = null;

        // When
        underTest.onPostDelete(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

}
