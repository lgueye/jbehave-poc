/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.persistence.events.PostDeleteAdvertEvent;
import org.diveintojee.poc.jbehave.persistence.events.PostDeleteAdvertEventListener;
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
public class PostDeleteAdvertEventListenerTest {

	@Mock
	private SearchEngine						searchEngine;

	@InjectMocks
	private final PostDeleteAdvertEventListener	underTest	= new PostDeleteAdvertEventListener();

	@Test
	public void onPostDeleteWillInvokeSearchEngine() {

		// Given
		final PostDeleteAdvertEvent event = Mockito.mock(PostDeleteAdvertEvent.class);
		final Advert advert = new Advert();
		advert.setId(5L);
		Mockito.when(event.getSource()).thenReturn(advert);

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verify(this.searchEngine).removeFromIndex(Advert.class, advert.getId());
		Mockito.verifyNoMoreInteractions(this.searchEngine);

	}

	@Test
	public void onPostDeleteWontInvokeSearchEngineWithNullEventEntity() {

		// Given
		final PostDeleteAdvertEvent event = Mockito.mock(PostDeleteAdvertEvent.class);
		Mockito.when(event.getSource()).thenReturn(null);

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verifyZeroInteractions(this.searchEngine);

	}

	@Test
	public void onPostDeleteWontInvokeSearchEngineWithNullEventEntityId() {

		// Given
		final PostDeleteAdvertEvent event = Mockito.mock(PostDeleteAdvertEvent.class);
		final Advert advert = new Advert();
		Mockito.when(event.getSource()).thenReturn(advert);

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verifyZeroInteractions(this.searchEngine);

	}

	@Test
	public void onPostDeleteWontInvokeSearchEngineWithNullInput() {

		// Given
		final PostDeleteAdvertEvent event = null;

		// When
		this.underTest.onApplicationEvent(event);

		// Then
		Mockito.verifyZeroInteractions(this.searchEngine);

	}

}
