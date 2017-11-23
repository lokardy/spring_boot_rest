/**
 * All rights are reserved. This code is developed by lokeshreddy2007@gmail.com.
 */
package com.mycompany.energyconsumption.service.test;

import java.time.Month;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.energyconsumption.entity.profile.Fraction;
import com.mycompany.energyconsumption.entity.profile.Profile;
import com.mycompany.energyconsumption.repository.ProfileRepository;
import com.mycompany.energyconsumption.starter.EnergyConsumptionApplication;
import com.mycompany.energyconsumuption.service.CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException;
import com.mycompany.energyconsumuption.service.InvalidConsumptionException;
import com.mycompany.energyconsumuption.service.MeterConfiguration;
import com.mycompany.energyconsumuption.service.MeterReadingConfiguration;
import com.mycompany.energyconsumuption.service.MeterService;
import com.mycompany.energyconsumuption.service.PowerConsumption;
import com.mycompany.energyconsumuption.service.ProfileNotFoundException;
import com.mycompany.energyconsumuption.service.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = EnergyConsumptionApplication.class)
@Transactional(propagation = Propagation.REQUIRED)
public class MeterConfigurationServiceTest {

	@Autowired
	private MeterService meterService;

	@Autowired
	private ProfileRepository profileRepository;

	private static final String PROFILE_NAME = "B";

	private static final String METER_ID = "002";

	private static final String UPDATE_METER_ID = "003";

	@Before
	public void beforeTests() throws ServiceException {
		Profile profile = new Profile();

		profile.setName(PROFILE_NAME);
		profile.getFractions().add(new Fraction(Month.JANUARY, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.FEBRUARY, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.MARCH, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.APRIL, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.MAY, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.JUNE, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.JULY, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.AUGUST, 0.1f, profile));
		profile.getFractions().add(new Fraction(Month.SEPTEMBER, 0.05f, profile));
		profile.getFractions().add(new Fraction(Month.OCTOBER, 0.05f, profile));
		profile.getFractions().add(new Fraction(Month.NOVEMBER, 0.05f, profile));
		profile.getFractions().add(new Fraction(Month.DECEMBER, 0.05f, profile));

		profileRepository.save(profile);

		MeterConfiguration meterConfiguration = new MeterConfiguration();

		meterConfiguration.setMeterId(UPDATE_METER_ID);
		meterConfiguration.setProfileName(PROFILE_NAME);

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 1);
		// MeterReadingConfiguration meterReadingConfiguration1 = new
		// MeterReadingConfiguration(Month.FEBRUARY, 2);
		MeterReadingConfiguration meterReadingConfiguration2 = new MeterReadingConfiguration(Month.FEBRUARY, 2);
		MeterReadingConfiguration meterReadingConfiguration3 = new MeterReadingConfiguration(Month.APRIL, 4);
		MeterReadingConfiguration meterReadingConfiguration4 = new MeterReadingConfiguration(Month.MARCH, 3);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);
		// meterConfiguration.getMeterReadings().add(meterReadingConfiguration1);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration2);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration3);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration4);

		meterService.createMeterConfiguration(meterConfiguration);

	}

	@After
	public void afterTests() throws ServiceException {

		meterService.deleteMeterConfiguration(METER_ID);
		meterService.deleteMeterConfiguration(UPDATE_METER_ID);

		Profile findByName = profileRepository.findByName(PROFILE_NAME);

		if (findByName != null) {

			profileRepository.delete(findByName);
		}

	}

	@Test
	public void shouldReturnCoumptionValueOfOneForTheMeterId003AndMonthFeb() throws ServiceException {

		PowerConsumption powerConsumption = meterService.findPowerConsumptionForTheGivenMeterIdAndMonth(UPDATE_METER_ID,
				Month.FEBRUARY);

		Assert.assertEquals(UPDATE_METER_ID, powerConsumption.getMeterId());
		Assert.assertEquals(Month.FEBRUARY, powerConsumption.getMonth());
		Assert.assertEquals(new Integer(1), powerConsumption.getConsumption());

	}
	
	@Test
	public void findPowerConsumptionShouldReturnInCaseOfInvalidId() throws ServiceException {

		PowerConsumption powerConsumption = meterService.findPowerConsumptionForTheGivenMeterIdAndMonth("3333",
				Month.FEBRUARY);

		Assert.assertNull(powerConsumption);
		

	}


	@Test
	public void shouldInsertMeterConfigurationSuccessfullyInCaseOfValidData() throws ServiceException {
		MeterConfiguration meterConfiguration = new MeterConfiguration();

		meterConfiguration.setMeterId(METER_ID);
		meterConfiguration.setProfileName(PROFILE_NAME);

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 1);
		// MeterReadingConfiguration meterReadingConfiguration1 = new
		// MeterReadingConfiguration(Month.FEBRUARY, 2);
		MeterReadingConfiguration meterReadingConfiguration2 = new MeterReadingConfiguration(Month.FEBRUARY, 2);
		MeterReadingConfiguration meterReadingConfiguration3 = new MeterReadingConfiguration(Month.APRIL, 4);
		MeterReadingConfiguration meterReadingConfiguration4 = new MeterReadingConfiguration(Month.MARCH, 3);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration2);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration3);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration4);

		meterConfiguration = meterService.createMeterConfiguration(meterConfiguration);

		Assert.assertEquals(METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfiguration,
				meterReadingConfiguration2, meterReadingConfiguration3, meterReadingConfiguration4));
	}

	@Test(expected = CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException.class)
	public void shouldThrowCurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueExceptionInCaseOfFebMonthReadingLessThanJan()
			throws ServiceException {
		MeterConfiguration meterConfiguration = new MeterConfiguration();

		meterConfiguration.setMeterId(METER_ID);
		meterConfiguration.setProfileName(PROFILE_NAME);

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 5);
		MeterReadingConfiguration meterReadingConfiguration2 = new MeterReadingConfiguration(Month.FEBRUARY, 2);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration2);

		meterService.createMeterConfiguration(meterConfiguration);

	}

	@Test(expected = InvalidConsumptionException.class)
	public void shouldThrowInvalidConsumptionExceptionInCaseofMonthlyQuotaExceedMinAndMaxConsumptionAsPerFractions()
			throws ServiceException {
		MeterConfiguration meterConfiguration = new MeterConfiguration();

		meterConfiguration.setMeterId(METER_ID);
		meterConfiguration.setProfileName(PROFILE_NAME);

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 5);
		MeterReadingConfiguration meterReadingConfiguration2 = new MeterReadingConfiguration(Month.FEBRUARY, 100);
		MeterReadingConfiguration meterReadingConfiguration3 = new MeterReadingConfiguration(Month.MARCH, 2000);
		MeterReadingConfiguration meterReadingConfiguration4 = new MeterReadingConfiguration(Month.MAY, 7000);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration2);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration3);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration4);

		meterService.createMeterConfiguration(meterConfiguration);

	}

	@Test(expected = ProfileNotFoundException.class)
	public void insertShouldThrowProfileNotFoundExceptionInCaseOfInvalidProfileName() throws ServiceException {
		MeterConfiguration meterConfiguration = new MeterConfiguration();

		meterConfiguration.setMeterId(METER_ID);
		meterConfiguration.setProfileName("errere");

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 1);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);

		meterConfiguration = meterService.createMeterConfiguration(meterConfiguration);

		Assert.assertEquals(METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfiguration));
	}

	@Test
	public void shouldUpdateMeterConfigurationSuccessfullyInCaseOfValidData() throws ServiceException {
		MeterConfiguration meterConfiguration = meterService.readMeterConfiguration(UPDATE_METER_ID);

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 1);
		// MeterReadingConfiguration meterReadingConfiguration1 = new
		// MeterReadingConfiguration(Month.FEBRUARY, 2);
		MeterReadingConfiguration meterReadingConfiguration2 = new MeterReadingConfiguration(Month.FEBRUARY, 2);
		MeterReadingConfiguration meterReadingConfiguration3 = new MeterReadingConfiguration(Month.APRIL, 4);
		MeterReadingConfiguration meterReadingConfiguration4 = new MeterReadingConfiguration(Month.MARCH, 3);

		meterConfiguration.getMeterReadings().clear();

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration2);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration3);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration4);

		meterConfiguration = meterService.updateMeterConfiguration(meterConfiguration);

		Assert.assertEquals(UPDATE_METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfiguration,
				meterReadingConfiguration2, meterReadingConfiguration3, meterReadingConfiguration4));
	}

	@Test
	public void shouldReadMeterConfigurationSuccessfullyForValidMeterId() throws ServiceException {
		MeterConfiguration meterConfiguration = meterService.readMeterConfiguration(UPDATE_METER_ID);

		MeterReadingConfiguration meterReadingConfigurationJan = new MeterReadingConfiguration(Month.JANUARY, 1);

		Assert.assertEquals(UPDATE_METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfigurationJan));
	}

	@Test
	public void shouldDeleteMeterConfigurationSuccessfullyForValidMeterId() throws ServiceException {
		MeterConfiguration meterConfiguration = meterService.deleteMeterConfiguration(UPDATE_METER_ID);

		MeterReadingConfiguration meterReadingConfigurationJan = new MeterReadingConfiguration(Month.JANUARY, 1);

		Assert.assertEquals(UPDATE_METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfigurationJan));
	}

	@Test
	public void shouldReturnNullIfInValidMeterIdIsGiven() throws ServiceException {
		MeterConfiguration meterConfiguration = meterService.deleteMeterConfiguration("000000");

		Assert.assertNull(meterConfiguration);
	}

}
