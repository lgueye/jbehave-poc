/*
 *
 */
package fr.explorimmo.poc.persistence.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.stereotype.Repository;

import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.persistence.JpaConstants;
import fr.explorimmo.poc.persistence.AdvertDao;

/**
 * @author louis.gueye@gmail.com
 */
@Repository(AdvertDao.BEAN_ID)
public class AdvertDaoImpl implements AdvertDao {

	@PersistenceContext(unitName = JpaConstants.PERSISTANCE_UNIT_NAME)
	private EntityManager	entityManager;

	/**
	 * @see fr.explorimmo.poc.persistence.AdvertDao#findByExample(fr.explorimmo.poc.domain.Advert)
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
