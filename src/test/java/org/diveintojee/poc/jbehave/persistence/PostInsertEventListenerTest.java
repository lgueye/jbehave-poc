/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Address;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.hibernate.event.PostInsertEvent;
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
public class PostInsertEventListenerTest {

    @Mock
    private SearchEngine searchEngine;

    @InjectMocks
    private final PostInsertEventListener underTest = new PostInsertEventListener();

    @Test
    public void onPostInsertWillInvokeSearchEngine() {

        // Given
        final PostInsertEvent event = Mockito.mock(PostInsertEvent.class);
        final Advert advert = new Advert();
        advert.setId(5L);
        Mockito.when(event.getEntity()).thenReturn(advert);

        // When
        underTest.onPostInsert(event);

        // Then
        Mockito.verify(searchEngine).index(Advert.class, advert);
        Mockito.verifyNoMoreInteractions(searchEngine);

    }

    @Test
    public void onPostInsertWontInvokeSearchEngineIfEventEntityIsNotAnAdvert() {

        // Given
        final PostInsertEvent event = Mockito.mock(PostInsertEvent.class);
        Mockito.when(event.getEntity()).thenReturn(new Address());

        // When
        underTest.onPostInsert(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

    @Test
    public void onPostInsertWontInvokeSearchEngineWithNullEventEntity() {

        // Given
        final PostInsertEvent event = Mockito.mock(PostInsertEvent.class);
        Mockito.when(event.getEntity()).thenReturn(null);

        // When
        underTest.onPostInsert(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

    @Test
    public void onPostInsertWontInvokeSearchEngineWithNullEventEntityId() {

        // Given
        final PostInsertEvent event = Mockito.mock(PostInsertEvent.class);
        final Advert advert = new Advert();
        Mockito.when(event.getEntity()).thenReturn(advert);

        // When
        underTest.onPostInsert(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

    @Test
    public void onPostInsertWontInvokeSearchEngineWithNullInput() {

        // Given
        final PostInsertEvent event = null;

        // When
        underTest.onPostInsert(event);

        // Then
        Mockito.verifyZeroInteractions(searchEngine);

    }

}
