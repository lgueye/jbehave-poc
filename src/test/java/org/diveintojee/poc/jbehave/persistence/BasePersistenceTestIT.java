/*
 *
 */
package org.diveintojee.poc.jbehave.persistence;

import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;

import org.diveintojee.poc.jbehave.domain.Persistable;
import org.diveintojee.poc.jbehave.domain.validation.ValidationContext;
import org.diveintojee.poc.jbehave.persistence.BaseDao;
import org.diveintojee.poc.jbehave.test.TestUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for database integration testing<br/>
 * Can not be instantiated<br/>
 * Does all the wiring plumbing<br/>
 * 
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jbehave-poc-server.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class BasePersistenceTestIT {

	@Autowired
	protected BaseDao		baseDao;

	@Autowired
	protected DataSource	dataSource;

	/**
	 * @param underTest
	 * @param context
	 */
	protected void assertExpectedViolation(final Persistable underTest, final ValidationContext context,
			String errorCode, String errorPath) {
		// When
		try {

			switch (context) {
				case CREATE:
				case UPDATE:
					this.baseDao.persist(underTest);
					break;
				case DELETE:
					this.baseDao.delete(underTest.getClass(), underTest.getId());
					break;
				default:
					throw new IllegalArgumentException("Unsupported validation context : " + context);
			}

			Assert.fail(ConstraintViolationException.class.getName() + " expected");

			// Then
		} catch (final ConstraintViolationException e) {
			TestUtils.assertViolationContainsTemplateAndPath(e, errorCode, errorPath);
		} catch (final Throwable th) {
			th.printStackTrace();
			Assert.fail(ConstraintViolationException.class.getName() + " expected, got class="
					+ th.getClass().getName() + ", message=" + th.getLocalizedMessage() + ", cause=" + th.getCause());
		}
	}

}
