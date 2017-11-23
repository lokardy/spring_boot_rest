package com.mycompany.energyconsumption.controller.test;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycompany.energyconsumption.controller.BulkMeterOperationResponse;
import com.mycompany.energyconsumption.controller.ErrorInfo;
import com.mycompany.energyconsumption.controller.MeterFinderMethodResponse;
import com.mycompany.energyconsumption.entity.profile.Fraction;
import com.mycompany.energyconsumption.entity.profile.Profile;
import com.mycompany.energyconsumption.repository.ProfileRepository;
import com.mycompany.energyconsumption.starter.EnergyConsumptionApplication;
import com.mycompany.energyconsumuption.service.MeterConfiguration;
import com.mycompany.energyconsumuption.service.MeterReadingConfiguration;
import com.mycompany.energyconsumuption.service.MeterService;
import com.mycompany.energyconsumuption.service.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		EnergyConsumptionApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeterIntegrationTests {
	// Either use scripts or services to do initial set up.
	@Autowired
	private MeterService meterService;

	@Autowired
	private TestRestTemplate template;

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

		Map<String, Object> parameters = new HashMap<>();

		// parameters.put("meterId", "003");
		parameters.put("month", Month.FEBRUARY);
		parameters.put("columns", "powerConsumption");

		MeterFinderMethodResponse meterFinderMethodResponse = template.getForEntity(
				"/meters/003?month={month}&columns={columns}", MeterFinderMethodResponse.class, parameters).getBody();

		Assert.assertEquals(UPDATE_METER_ID, meterFinderMethodResponse.getMeterId());
		Assert.assertEquals(Month.FEBRUARY, meterFinderMethodResponse.getMonth());
		Assert.assertEquals(new Integer(1), meterFinderMethodResponse.getPowerConsumption());

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

		meterConfiguration = template.postForEntity("/meters", meterConfiguration, MeterConfiguration.class).getBody();

		Assert.assertEquals(METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfiguration,
				meterReadingConfiguration2, meterReadingConfiguration3, meterReadingConfiguration4));
	}

	@Test
	public void shouldInsertOneRecordAndRejectOneForValidAndInvalidCase() throws ServiceException {
		List<MeterConfiguration> meterConfigurations = new ArrayList<MeterConfiguration>();
				
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

		MeterConfiguration meterConfiguration1 = new MeterConfiguration();

		meterConfiguration1.setMeterId("008");
		meterConfiguration1.setProfileName("DD");

		MeterReadingConfiguration meterReadingConfiguration21 = new MeterReadingConfiguration(Month.JANUARY, 5);
		MeterReadingConfiguration meterReadingConfiguration22 = new MeterReadingConfiguration(Month.FEBRUARY, 2);

		meterConfiguration1.getMeterReadings().add(meterReadingConfiguration21);
		meterConfiguration1.getMeterReadings().add(meterReadingConfiguration22);
		
		meterConfigurations.add(meterConfiguration);
		meterConfigurations.add(meterConfiguration1);
		//header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//person list
		
	
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(meterConfigurations,headers);
		
		BulkMeterOperationResponse bulkMeterOperationResponse = template.exchange("/meters-collection", HttpMethod.POST, requestEntity, BulkMeterOperationResponse.class).getBody();
		
		Assert.assertEquals(1, bulkMeterOperationResponse.getSuccessCount());
		Assert.assertEquals(1, bulkMeterOperationResponse.getFailureCount());
	}
	@Test
	public void shouldThrowCurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueExceptionInCaseOfFebMonthReadingLessThanJan()
			throws ServiceException {
		MeterConfiguration meterConfiguration = new MeterConfiguration();

		meterConfiguration.setMeterId(METER_ID);
		meterConfiguration.setProfileName(PROFILE_NAME);

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 5);
		MeterReadingConfiguration meterReadingConfiguration2 = new MeterReadingConfiguration(Month.FEBRUARY, 2);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);
		meterConfiguration.getMeterReadings().add(meterReadingConfiguration2);

		ErrorInfo error = template.postForEntity("/meters", meterConfiguration, ErrorInfo.class).getBody();

		Assert.assertEquals("0003", error.getErrorKey());

	}
	
	@Test
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

		ErrorInfo error = template.postForEntity("/meters", meterConfiguration, ErrorInfo.class).getBody();
		
		Assert.assertEquals("0005", error.getErrorKey());
	}
	
	@Test
	public void insertShouldThrowProfileNotFoundExceptionInCaseOfInvalidProfileName() throws ServiceException {
		MeterConfiguration meterConfiguration = new MeterConfiguration();

		meterConfiguration.setMeterId(METER_ID);
		meterConfiguration.setProfileName("errere");

		MeterReadingConfiguration meterReadingConfiguration = new MeterReadingConfiguration(Month.JANUARY, 1);

		meterConfiguration.getMeterReadings().add(meterReadingConfiguration);

		ErrorInfo error = template.postForEntity("/meters", meterConfiguration, ErrorInfo.class).getBody();

		Assert.assertEquals("0002", error.getErrorKey());
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

		template.put("/meters/003", meterConfiguration);

		meterConfiguration = template.getForEntity("/meters/003", MeterConfiguration.class).getBody();

		Assert.assertEquals(UPDATE_METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfiguration,
				meterReadingConfiguration2, meterReadingConfiguration3, meterReadingConfiguration4));
	}
	
	@Test
	public void shouldReadMeterConfigurationSuccessfullyForValidMeterId() throws ServiceException {
		MeterConfiguration meterConfiguration = template.getForEntity("/meters/003", MeterConfiguration.class).getBody();

		MeterReadingConfiguration meterReadingConfigurationJan = new MeterReadingConfiguration(Month.JANUARY, 1);

		Assert.assertEquals(UPDATE_METER_ID, meterConfiguration.getMeterId());
		Assert.assertEquals(PROFILE_NAME, meterConfiguration.getProfileName());
		Assert.assertThat(meterConfiguration.getMeterReadings(), CoreMatchers.hasItems(meterReadingConfigurationJan));
	}

	@Test
	public void shouldDeleteMeterConfigurationSuccessfullyForValidMeterId() throws ServiceException {
		template.delete("/meters/003");
		
		MeterConfiguration meterConfiguration = meterService.readMeterConfiguration("003");
		
		Assert.assertNull(meterConfiguration);

		
	}

}
