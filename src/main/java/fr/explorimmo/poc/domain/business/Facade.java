/*
 *
 */
package fr.explorimmo.poc.domain.business;

import java.util.List;

import javax.validation.ConstraintViolationException;

import fr.explorimmo.poc.domain.AbstractEntity;
import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.domain.validation.ValidationContext;

/**
 * @author louis.gueye@gmail.com
 */
public interface Facade {

	String	BEAN_ID	= "facade";

	/**
	 * @param advert
	 * @return
	 * @throws ConstraintViolationException
	 */
	Long createAdvert(Advert advert);

	/**
	 * @param advertId
	 * @throws ConstraintViolationException
	 */
	void deleteAdvert(Long advertId);

	/**
	 * @param criteria
	 * @return
	 */
	List<Advert> findAdvertsByCriteria(Advert criteria);

	/**
	 * @param advertId
	 * @return
	 */
	Advert readAdvert(Long advertId);

	/**
	 * @param advert
	 * @throws ConstraintViolationException
	 */
	void updateAdvert(Advert advert);

	/**
	 * @param <T>
	 * @param type
	 * @param context
	 * @throws ConstraintViolationException
	 */
	<T extends AbstractEntity> void validate(T type, ValidationContext context);

}
