/*
 *
 */
package org.diveintojee.poc.jbehave.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.diveintojee.poc.jbehave.domain.Advert;
import org.diveintojee.poc.jbehave.persistence.AdvertDao;
import org.diveintojee.poc.jbehave.persistence.JpaConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Repository;


/**
 * @author louis.gueye@gmail.com
 */
@Repository(AdvertDao.BEAN_ID)
public class AdvertDaoImpl implements AdvertDao {

	@PersistenceContext(unitName = JpaConstants.PERSISTANCE_UNIT_NAME)
	private EntityManager	entityManager;

	/**
	 * @see org.diveintojee.poc.jbehave.persistence.AdvertDao#findByExample(org.diveintojee.poc.jbehave.domain.Advert)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Advert> findByExample(final Advert exampleInstance) {

		final Session session = (Session) this.entityManager.getDelegate();

		final Example example = Example.create(exampleInstance).excludeZeroes() // exclude
																				// zero
																				// valued
																				// properties
				.ignoreCase() // perform case insensitive string comparisons
				.enableLike(MatchMode.ANYWHERE); // use like for string
													// comparisons

		final Criteria advertCriteria = session.createCriteria(exampleInstance.getClass());

		advertCriteria.add(example);

		advertCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		return advertCriteria.list();

	}
}
