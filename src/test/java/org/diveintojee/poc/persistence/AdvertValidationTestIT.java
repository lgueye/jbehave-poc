/*
 *
 */
package org.diveintojee.poc.persistence;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.diveintojee.poc.domain.Address;
import org.diveintojee.poc.domain.Advert;
import org.diveintojee.poc.domain.validation.ValidationContext;
import org.diveintojee.poc.test.TestUtils;
import org.junit.Test;


/**
 * Advert validation testing<br/>
 * CRUD operations are tested<br>
 * 
 * @author louis.gueye@gmail.com
 */
public class AdvertValidationTestIT extends BasePersistenceTestIT {

	private Advert	underTest	= null;

	/**
	 * Validate "create advert use case"
	 */
	@Test
	public void shouldValidateAdvertForCreateContext() {

		shouldValidateNameRequiredConstraint(ValidationContext.CREATE);

		shouldValidateNameSizeConstraint(ValidationContext.CREATE);

		shouldValidateDescriptionSizeConstraint(ValidationContext.CREATE);

		shouldValidateEmailRequiredConstraint(ValidationContext.CREATE);

		shouldValidateEmailValidFormatConstraint(ValidationContext.CREATE);

		shouldValidatePhoneNumberRequiredConstraint(ValidationContext.CREATE);

		shouldValidatePhoneNumberSizeConstraint(ValidationContext.CREATE);

		shouldValidateAddressRequiredConstraint(ValidationContext.CREATE);

		shouldValidateStreetAddressRequiredConstraint(ValidationContext.CREATE);

		shouldValidateStreetAddressSizeConstraint(ValidationContext.CREATE);

		shouldValidateCityRequiredConstraint(ValidationContext.CREATE);

		shouldValidateCitySizeConstraint(ValidationContext.CREATE);

		shouldValidatePostalCodeRequiredConstraint(ValidationContext.CREATE);

		shouldValidatePostalCodeSizeConstraint(ValidationContext.CREATE);

		shouldValidateCountryCodeRequiredConstraint(ValidationContext.CREATE);

		shouldValidateCountryCodeSizeConstraint(ValidationContext.CREATE);

	}

	/**
	 * Validate "create advert use case"
	 */
	@Test
	public void shouldValidateAdvertForUpdateContext() {

		shouldValidateNameRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateNameSizeConstraint(ValidationContext.UPDATE);

		shouldValidateDescriptionSizeConstraint(ValidationContext.UPDATE);

		shouldValidateEmailRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateEmailValidFormatConstraint(ValidationContext.UPDATE);

		shouldValidatePhoneNumberRequiredConstraint(ValidationContext.UPDATE);

		shouldValidatePhoneNumberSizeConstraint(ValidationContext.UPDATE);

		shouldValidateAddressRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateStreetAddressRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateStreetAddressSizeConstraint(ValidationContext.UPDATE);

		shouldValidateCityRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateCitySizeConstraint(ValidationContext.UPDATE);

		shouldValidatePostalCodeRequiredConstraint(ValidationContext.UPDATE);

		shouldValidatePostalCodeSizeConstraint(ValidationContext.UPDATE);

		shouldValidateCountryCodeRequiredConstraint(ValidationContext.UPDATE);

		shouldValidateCountryCodeSizeConstraint(ValidationContext.UPDATE);

	}

	/**
	 * Given : a valid advert valued with an invalid name<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateNameRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = null;
		this.underTest.setName(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.name.required}", "name");

	}

	/**
	 * Given : a valid advert valued with an invalid name<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateNameSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = RandomStringUtils.random(Advert.CONSTRAINT_NAME_MAX_SIZE + 1);
		this.underTest.setName(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.name.max.size}", "name");

	}

	/**
	 * Given : a valid advert valued with an invalid phone number<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePhoneNumberRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = null;
		this.underTest.setPhoneNumber(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.phoneNumber.required}", "phoneNumber");

	}

	/**
	 * Given : a valid advert valued with an invalid address<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateAddressRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final Address wrongData = null;
		this.underTest.setAddress(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.address.required}", "address");

	}

	/**
	 * Given : a valid advert valued with an invalid street address<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateStreetAddressRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = null;
		this.underTest.getAddress().setStreetAddress(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.streetAddress.required}", "address.streetAddress");

	}

	/**
	 * Given : a valid advert valued with an invalid street address<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateStreetAddressSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_STREET_ADDRESS_MAX_SIZE + 1);
		this.underTest.getAddress().setStreetAddress(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.streetAddress.max.size}", "address.streetAddress");

	}

	/**
	 * Given : a valid advert valued with an invalid city<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCityRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = null;
		this.underTest.getAddress().setCity(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.city.required}", "address.city");

	}

	/**
	 * Given : a valid advert valued with an invalid city<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCitySizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_CITY_MAX_SIZE + 1);
		this.underTest.getAddress().setCity(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.city.max.size}", "address.city");

	}

	/**
	 * Given : a valid advert valued with an invalid postal code<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePostalCodeRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = null;
		this.underTest.getAddress().setPostalCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.postalCode.required}", "address.postalCode");

	}

	/**
	 * Given : a valid advert valued with an invalid postal code<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePostalCodeSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_POSTAL_CODE_MAX_SIZE + 1);
		this.underTest.getAddress().setPostalCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.postalCode.max.size}", "address.postalCode");

	}

	/**
	 * Given : a valid advert valued with an invalid country code<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCountryCodeRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = null;
		this.underTest.getAddress().setCountryCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.countryCode.required}", "address.countryCode");

	}

	/**
	 * Given : a valid advert valued with an invalid country code<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateCountryCodeSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = RandomStringUtils.random(Address.CONSTRAINT_COUNTRY_CODE_MAX_SIZE + 1);
		this.underTest.getAddress().setCountryCode(wrongData);

		assertExpectedViolation(this.underTest, context, "{address.countryCode.exact.size}", "address.countryCode");

	}

	/**
	 * Given : a valid advert valued with an invalid phone number<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidatePhoneNumberSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = RandomStringUtils.random(Advert.CONSTRAINT_PHONE_NUMBER_MAX_SIZE + 1);
		this.underTest.setPhoneNumber(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.phoneNumber.max.size}", "phoneNumber");

	}

	/**
	 * Given : a valid advert valued with an invalid description<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateDescriptionSizeConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = RandomStringUtils.random(Advert.CONSTRAINT_DESCRIPTION_MAX_SIZE + 1);
		this.underTest.setDescription(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.description.max.size}", "description");

	}

	/**
	 * Given : a valid advert valued with an invalid email<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateEmailRequiredConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = null;
		this.underTest.setEmail(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.email.required}", "email");

	}

	/**
	 * Given : a valid advert valued with an invalid email<br/>
	 * When : one persists the above advert<br/>
	 * Then : system should throw a {@link ConstraintViolationException}<br/>
	 */
	private void shouldValidateEmailValidFormatConstraint(final ValidationContext context) {
		// Given
		this.underTest = TestUtils.validAdvert();
		final String wrongData = "foo.bar";
		this.underTest.setEmail(wrongData);

		assertExpectedViolation(this.underTest, context, "{advert.email.valid.format.required}", "email");

	}
}
