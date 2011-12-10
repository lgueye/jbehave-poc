/**
 * 
 */
package org.diveintojee.poc.persistence.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.diveintojee.poc.persistence.BaseDao;
import org.diveintojee.poc.persistence.JpaConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;


/**
 * @author louis.gueye@gmail.com
 */
@Repository(BaseDao.BEAN_ID)
public class BaseDaoImpl implements BaseDao {

    @PersistenceContext(unitName = JpaConstants.PERSISTANCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#countAll(java.lang.Class)
     */
    @Override
    public <T> int countAll(final Class<T> entityClass) {
        return countByCriteria(entityClass);
    }

    /**
     * @param <T>
     * @param entityClass
     * @param criterion
     * @return
     */
    protected <T> int countByCriteria(final Class<T> entityClass, final Criterion... criterion) {
        final Session session = (Session) entityManager.getDelegate();
        final Criteria crit = session.createCriteria(entityClass);
        crit.setProjection(Projections.rowCount());

        for (final Criterion c : criterion) {
            crit.add(c);
        }

        return ((Long) crit.list().get(0)).intValue();
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#countByExample(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> int countByExample(final T exampleInstance) {

        final Session session = (Session) entityManager.getDelegate();

        final Example example = Example.create(exampleInstance).excludeZeroes() // exclude zero valued properties
                .ignoreCase() // perform case insensitive string comparisons
                .enableLike(); // use like for string comparisons

        final Criteria criteria = session.createCriteria(exampleInstance.getClass());

        criteria.setProjection(Projections.rowCount());

        criteria.add(example);

        final List<Integer> list = criteria.list();

        return list.get(0);

    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#delete(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> void delete(final Class<T> entityClass, final Object id) {
        final Object entity = get(entityClass, id);
        entityManager.remove(entity);
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#evict(java.lang.Object)
     */
    @Override
    public void evict(final Object attachedInstance) {
        ((Session) entityManager.getDelegate()).evict(attachedInstance);
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#findAll(java.lang.Class)
     */
    @Override
    public <T> List<T> findAll(final Class<T> entityClass) {
        return findByCriteria(entityClass);
    }

    /**
     * Use this inside subclasses as a convenience method.
     * 
     * @param <T>
     * @param entityClass
     * @param criterion
     * @return
     */
    protected <T> List<T> findByCriteria(final Class<T> entityClass, final Criterion... criterion) {
        return findByCriteria(entityClass, -1, -1, criterion);
    }

    /**
     * Use this inside subclasses as a convenience method.
     * 
     * @param <T>
     * @param entityClass
     * @param firstResult
     * @param maxResults
     * @param criterion
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByCriteria(final Class<T> entityClass, final int firstResult, final int maxResults,
            final Criterion... criterion) {
        final Session session = (Session) entityManager.getDelegate();
        final Criteria crit = session.createCriteria(entityClass);

        for (final Criterion c : criterion) {
            crit.add(c);
        }

        if (firstResult > 0) {
            crit.setFirstResult(firstResult);
        }

        if (maxResults > 0) {
            crit.setMaxResults(maxResults);
        }

        final List<T> result = crit.list();
        return result;
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#findByExample(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByExample(final T exampleInstance) {

        final Session session = (Session) entityManager.getDelegate();

        final Example example = Example.create(exampleInstance).excludeZeroes() // exclude zero valued properties
                .ignoreCase() // perform case insensitive string comparisons
                .enableLike(MatchMode.ANYWHERE); // use like for string comparisons

        final Criteria criteria = session.createCriteria(exampleInstance.getClass());

        criteria.add(example);

        return criteria.list();

    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#findByNamedQuery(java.lang.String, java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByNamedQuery(final String name, final Object... params) {
        final javax.persistence.Query query = entityManager.createNamedQuery(name);

        for (int i = 0; i < params.length; i++) {
            query.setParameter((i + 1), params[i]);
        }

        final List<T> result = query.getResultList();
        return result;
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#findByNamedQueryAndNamedParams(java.lang.String, java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByNamedQueryAndNamedParams(final String name, final Map<String, ? extends Object> params) {
        final javax.persistence.Query query = entityManager.createNamedQuery(name);

        for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        final List<T> result = query.getResultList();
        return result;
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#flush()
     */
    @Override
    public void flush() {
        entityManager.flush();
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#get(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> T get(final Class<T> entityClass, final Object id) {
        return entityManager.find(entityClass, id);
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#merge(java.lang.Object)
     */
    @Override
    public void merge(final Object entity) {
        entityManager.merge(entity);
    }

    /**
     * @see org.diveintojee.poc.persistence.BaseDao#persist(java.lang.Object)
     */
    @Override
    public void persist(final Object entity) {
        entityManager.persist(entity);
    }

}
