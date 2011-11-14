/*
 *
 */
package fr.explorimmo.poc.business;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.domain.business.Facade;
import fr.explorimmo.poc.test.TestUtils;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:explorimmo-server.xml" })
public class FacadeImplTestIT {

	@Autowired
	private Facade	facade;

	@Test
	public void createEntityShouldPersistAndSetId() throws Throwable {
		// Given
		final Advert advert = TestUtils.validAdvert();
		// ensure id nullity
		advert.setId(null);
		// When
		final Long id = this.facade.createAdvert(advert);

		// Then
		Assert.assertNotNull(id);
		Assert.assertEquals(id, advert.getId());

	}

	@Test
	public void updateEntityShouldPersistProperties() throws Throwable {
		// Given
		Advert advert = TestUtils.validAdvert();
		advert.setId(null);
		// When
		final Long id = this.facade.createAdvert(advert);
		// Then
		Assert.assertNotNull(id);
		Assert.assertEquals(id, advert.getId());
		final String newName = "New name";
		final String newDescription = "Brand New description";

		// Given
		advert.setName(newName);
		advert.setDescription(newDescription);

		// When
		this.facade.updateAdvert(advert);

		advert = this.facade.readAdvert(id);

		// Then
		Assert.assertEquals(newName, advert.getName());
		Assert.assertEquals(newDescription, advert.getDescription());

	}

	@Test
	public void deleteEntityShouldSucceed() throws Throwable {
		// Given
		Advert advert = TestUtils.validAdvert();
		advert.setId(null);
		// When
		final Long id = this.facade.createAdvert(advert);
		// Then
		Assert.assertNotNull(id);
		Assert.assertEquals(id, advert.getId());

		// When
		this.facade.deleteAdvert(advert.getId());

		// Then
		Assert.assertNull(this.facade.readAdvert(id));
	}

}
