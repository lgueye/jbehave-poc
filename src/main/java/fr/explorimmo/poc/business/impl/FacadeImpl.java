/*
 *
 */
package fr.explorimmo.poc.business.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import fr.explorimmo.poc.domain.AbstractEntity;
import fr.explorimmo.poc.domain.Advert;
import fr.explorimmo.poc.domain.business.Facade;
import fr.explorimmo.poc.domain.validation.ValidationContext;
import fr.explorimmo.poc.persistence.BaseDao;

/**
 * @author louis.gueye@gmail.com
 */
@Service(Facade.BEAN_ID)
public class FacadeImpl implements Facade {

	@Autowired
	private Validator	validator;

	@Autowired
	private BaseDao		baseDao;

	/**
	 * @see fr.explorimmo.poc.domain.business.Facade#createAdvert(fr.explorimmo.poc.domain.Advert)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long createAdvert(final Advert advert) {

		Preconditions.checkArgument(advert != null, "Illegal call to createAdvert, advert is required");

		this.baseDao.persist(advert);

		return advert.getId();

	}

	/**
	 * @see fr.explorimmo.poc.domain.business.Facade#deleteAdvert(java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAdvert(final Long advertId) {

		Preconditions.checkArgument(advertId != null, "Illegal call to deleteAdvert, advert identifier is required");

		this.baseDao.delete(Advert.class, advertId);

	}

	/**
	 * @see fr.explorimmo.poc.domain.business.Facade#findAdvertsByCriteria(fr.explorimmo.poc.domain.Advert)
	 */
	@Override
	public List<Advert> findAdvertsByCriteria(final Advert criteria) {

		Preconditions.checkArgument(criteria != null, "Illegal call to findAdvertsByCriteria, criteria is required");

		return this.baseDao.findByExample(criteria);

	}

	/**
	 * @see fr.explorimmo.poc.domain.business.Facade#readAdvert(java.lang.Long)
	 */
	@Override
	public Advert readAdvert(final Long advertId) {

		Preconditions.checkArgument(advertId != null, "Illegal call to readAdvert, advert identifier is required");

		return this.baseDao.get(Advert.class, advertId);

	}

	/**
	 * @see fr.explorimmo.poc.domain.business.Facade#updateAdvert(fr.explorimmo.poc.domain.Advert)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAdvert(final Advert advert) {

		Preconditions.checkArgument(advert != null, "Illegal call to updateAdvert, advert is required");

		Preconditions.checkArgument(advert.getId() != null, "Illegal call to updateAdvert, advert.id is required");

		final Advert persistedInstance = this.baseDao.get(Advert.class, advert.getId());

		Preconditions.checkState(persistedInstance != null,
				"Illegal call to updateAdvert, provided id should have corresponding advert in the store");

		persistedInstance.setAddress(advert.getAddress());

		persistedInstance.setDescription(advert.getDescription());

		persistedInstance.setEmail(advert.getEmail());

		persistedInstance.setName(advert.getName());

		persistedInstance.setPhoneNumber(advert.getPhoneNumber());

	}

	/**
	 * @see fr.explorimmo.poc.domain.business.Facade#validate(fr.explorimmo.poc.domain.AbstractEntity,
	 *      fr.explorimmo.poc.domain.validation.ValidationContext)
	 */
	@Override
	public <T extends AbstractEntity> void validate(final T type, final ValidationContext context) {

		Preconditions.checkArgument(type != null, "Illegal call to validate, object is required");

		Preconditions.checkArgument(context != null, "Illegal call to validate, validation context is required");

		final Set<ConstraintViolation<T>> constraintViolations = this.validator.validate(type, context.getContext());

		if (CollectionUtils.isEmpty(constraintViolations)) return;

		final Set<ConstraintViolation<?>> propagatedViolations = new HashSet<ConstraintViolation<?>>(
				constraintViolations.size());

		final Set<String> classNames = new HashSet<String>();

		for (final ConstraintViolation<?> violation : constraintViolations) {

			propagatedViolations.add(violation);

			classNames.add(violation.getLeafBean().getClass().getName());

		}

		throw new ConstraintViolationException(propagatedViolations);

	}

}
