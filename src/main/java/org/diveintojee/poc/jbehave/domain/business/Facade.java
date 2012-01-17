/*
 *
 */
package org.diveintojee.poc.jbehave.domain.business;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.diveintojee.poc.jbehave.domain.AbstractEntity;
import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.domain.OrderBy;
import org.diveintojee.poc.jbehave.domain.SearchResult;
import org.diveintojee.poc.jbehave.domain.validation.ValidationContext;

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

	/**
	 * @param criteria
	 * @return
	 */
	List<Advert> findProtectedAdvertsByCriteria(Advert criteria);

	/**
	 * @param query
	 * @param sort
	 * @param maxResults
	 * @param from
	 * @param itemsPerPage
	 * @return
	 */
	SearchResult findAdvertsByCriteria(String queryString, Set<OrderBy> orderByList, int startPage, int itemsPerPage);

}
