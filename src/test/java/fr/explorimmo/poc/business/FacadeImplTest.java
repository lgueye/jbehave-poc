/**
 * 
 */
package fr.explorimmo.poc.business;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.validator.engine.ConstraintViolationImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.explorimmo.poc.business.impl.FacadeImpl;
import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.domain.Persistable;
import fr.explorimmo.poc.domain.business.Facade;
import fr.explorimmo.poc.domain.validation.ValidationContext;
import fr.explorimmo.poc.persistence.BaseDao;

/**
 * @author louis.gueye@gmail.com
 */
public class FacadeImplTest {

	@Mock
	private Validator		validator;

	@Mock
	private BaseDao			baseDao;

	@InjectMocks
	private final Facade	underTest	= new FacadeImpl();

	@Test
	public void createAdvertShouldInvokePersistence() {

		// Given
		final Advert advert = Mockito.mock(Advert.class);

		// When
		this.underTest.createAdvert(advert);

		// Then
		Mockito.verify(this.baseDao).persist(advert);

		Mockito.verify(advert).getId();

	}

	@Test(expected = IllegalArgumentException.class)
	public void createAdvertShouldThrowIllegalArgumentExceptionWithNullAdvert() {

		// Given
		final Advert advert = null;

		// When
		this.underTest.createAdvert(advert);

	}

	@Test
	public void deleteAdvertShouldInvokePersistence() {

		// Given
		final Long advertId = 5L;

		// When
		this.underTest.deleteAdvert(advertId);

		// Then
		Mockito.verify(this.baseDao).delete(Advert.class, advertId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteAdvertShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long advertId = null;

		// When
		this.underTest.deleteAdvert(advertId);

	}

	@Test
	public void findAdvertByCriteriaShouldInvokePersistence() {

		// Given
		final Advert advert = Mockito.mock(Advert.class);

		// When
		this.underTest.findAdvertsByCriteria(advert);

		// Then
		Mockito.verify(this.baseDao).findByExample(advert);

	}

	@Test(expected = IllegalArgumentException.class)
	public void findAdvertByCriteriaShouldThrowIllegalArgumentExceptionWithNullAdvert() {

		// Given
		final Advert advert = null;

		// When
		this.underTest.findAdvertsByCriteria(advert);

	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void readAdvertShouldInvokePersistence() {

		// Given
		final Long advertId = 5L;

		// When
		this.underTest.readAdvert(advertId);

		// Then
		Mockito.verify(this.baseDao).get(Advert.class, advertId);

	}

	@Test(expected = IllegalArgumentException.class)
	public void readAdvertShouldThrowIllegalArgumentExceptionWithNullId() {

		// Given
		final Long advertId = null;

		// When
		this.underTest.readAdvert(advertId);

	}

	@Test
	public void updateAdvertShouldSetPropertiesThenInvokePersistence() {

		// Given
		final Advert advert = Mockito.mock(Advert.class);

		final Advert persistedInstance = Mockito.mock(Advert.class);

		Mockito.when(this.baseDao.get(Matchers.eq(Advert.class), Matchers.any(Long.class))).thenReturn(
				persistedInstance);

		// When
		this.underTest.updateAdvert(advert);

		// Then
		Mockito.verify(persistedInstance).setAddress(advert.getAddress());

		Mockito.verify(persistedInstance).setDescription(advert.getDescription());

		Mockito.verify(persistedInstance).setEmail(advert.getEmail());

		Mockito.verify(persistedInstance).setName(advert.getName());

		Mockito.verify(persistedInstance).setPhoneNumber(advert.getPhoneNumber());

	}

	@Test(expected = IllegalArgumentException.class)
	public void updateAdvertShouldThrowIllegalArgumentExceptionWithNullFoodSpecialty() {

		// Given
		final Advert advert = null;

		// When
		this.underTest.updateAdvert(advert);

	}

	@Test(expected = IllegalStateException.class)
	public void updateAdvertShouldThrowIllegalStateExceptionWithNullPersistedInstance() {

		// Given
		final Advert advert = new Advert();

		advert.setId(3L);

		Mockito.when(this.baseDao.get(Matchers.eq(Advert.class), Matchers.any(Long.class))).thenReturn(null);

		// When
		this.underTest.updateAdvert(advert);

	}

	@Test
	public void validateWillNotThrowExceptionWithEmptyViolationsSet() {
		// Given
		final Advert toBeValidated = new Advert();
		final ValidationContext validationContext = ValidationContext.CREATE;
		final Set<ConstraintViolation<Persistable>> violations = null;
		Mockito.when(this.validator.validate(Matchers.any(Persistable.class), Matchers.any(Class[].class))).thenReturn(
				violations);

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}

	@Test(expected = ConstraintViolationException.class)
	public void validateWillThrowExceptionWithNonEmptyViolationsSet() {
		// Given
		final Advert toBeValidated = new Advert();
		final ValidationContext validationContext = ValidationContext.CREATE;
		final Set<ConstraintViolation<Persistable>> violations = new HashSet<ConstraintViolation<Persistable>>();
		violations.add(new ConstraintViolationImpl<Persistable>("{message.template}", "interpolated message",
				Persistable.class, null, String.class, null, null, null, null));
		Mockito.when(this.validator.validate(Matchers.any(Persistable.class), Matchers.any(Class[].class))).thenReturn(
				violations);

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateWillThrowIllegalArgumentExceptionWithNullContext() {
		// Given
		final Advert toBeValidated = new Advert();
		final ValidationContext validationContext = null;

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}

	@Test(expected = IllegalArgumentException.class)
	public void validateWillThrowIllegalArgumentExceptionWithNullObject() {
		// Given
		final Advert toBeValidated = null;
		final ValidationContext validationContext = ValidationContext.DELETE;

		// When
		this.underTest.validate(toBeValidated, validationContext);
	}
}
