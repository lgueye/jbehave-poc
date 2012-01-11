/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.persistence.events.PostStoreAdvertEvent;
import org.diveintojee.poc.jbehave.persistence.events.PostStoreAdvertEventListener;
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
public class PostStoreAdvertEventListenerTest {

	@Mock
	private SearchEngine						searchEngine;

	@InjectMocks
	private final PostStoreAdvertEventListener	underTest	= new PostStoreAdvertEventListener();

	@Test
	public void onApplicationEventWillInvokeSearchEngine() {

		// Given
		final PostStoreAdvertEvent event = Mockito.mock(PostStoreAdvertEvent.class);
		final Advert advert = new Advert();
		advert.setId(5L);
		Mockito.when(event.getSource()).thenReturn(advert);

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verify(this.searchEngine).index(Advert.class, advert);
		Mockito.verifyNoMoreInteractions(this.searchEngine);

	}

	@Test
	public void onApplicationEventWontInvokeSearchEngineWithNullEventEntity() {

		// Given
		final PostStoreAdvertEvent event = Mockito.mock(PostStoreAdvertEvent.class);
		Mockito.when(event.getSource()).thenReturn(null);

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verifyZeroInteractions(this.searchEngine);

	}

	@Test
	public void onApplicationEventWontInvokeSearchEngineWithNullEventEntityId() {

		// Given
		final PostStoreAdvertEvent event = Mockito.mock(PostStoreAdvertEvent.class);
		final Advert advert = new Advert();
		Mockito.when(event.getSource()).thenReturn(advert);

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verifyZeroInteractions(this.searchEngine);

	}

	@Test
	public void onApplicationEventWontInvokeSearchEngineWithNullInput() {

		// Given
		final PostStoreAdvertEvent event = null;

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verifyZeroInteractions(this.searchEngine);

	}

}
