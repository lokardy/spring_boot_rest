package com.mycompany.energyconsumption.controller.test;

import java.time.Month;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.mycompany.energyconsumption.controller.ErrorInfo;
import com.mycompany.energyconsumption.starter.EnergyConsumptionApplication;
import com.mycompany.energyconsumuption.service.FractionConfiguration;
import com.mycompany.energyconsumuption.service.ProfileConfiguration;
import com.mycompany.energyconsumuption.service.ProfileService;
import com.mycompany.energyconsumuption.service.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		EnergyConsumptionApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ProfileIntegrationTests {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private ProfileService profileService;

	@Before
	public void beforeTests() throws ServiceException {

		ProfileConfiguration profileConfiguration = new ProfileConfiguration();

		profileConfiguration.setName("B");

		FractionConfiguration fractionConfigurationJan = new FractionConfiguration(Month.JANUARY, 0.1F);
		FractionConfiguration fractionConfigurationFeb = new FractionConfiguration(Month.FEBRUARY, 0.1F);
		FractionConfiguration fractionConfigurationMar = new FractionConfiguration(Month.MARCH, 0.1F);
		FractionConfiguration fractionConfigurationApr = new FractionConfiguration(Month.APRIL, 0.1F);
		FractionConfiguration fractionConfigurationMay = new FractionConfiguration(Month.MAY, 0.1F);
		FractionConfiguration fractionConfigurationJune = new FractionConfiguration(Month.JUNE, 0.1F);
		FractionConfiguration fractionConfigurationJuly = new FractionConfiguration(Month.JULY, 0.1F);
		FractionConfiguration fractionConfigurationAug = new FractionConfiguration(Month.AUGUST, 0.1F);
		FractionConfiguration fractionConfigurationSep = new FractionConfiguration(Month.SEPTEMBER, 0.1F);
		FractionConfiguration fractionConfigurationOct = new FractionConfiguration(Month.OCTOBER, 0.1F);
		FractionConfiguration fractionConfigurationNov = new FractionConfiguration(Month.NOVEMBER, 0F);
		FractionConfiguration fractionConfigurationDec = new FractionConfiguration(Month.DECEMBER, 0F);

		profileConfiguration.getFractions().add(fractionConfigurationJan);
		profileConfiguration.getFractions().add(fractionConfigurationFeb);
		profileConfiguration.getFractions().add(fractionConfigurationMar);
		profileConfiguration.getFractions().add(fractionConfigurationApr);
		profileConfiguration.getFractions().add(fractionConfigurationMay);
		profileConfiguration.getFractions().add(fractionConfigurationJune);
		profileConfiguration.getFractions().add(fractionConfigurationJuly);
		profileConfiguration.getFractions().add(fractionConfigurationAug);
		profileConfiguration.getFractions().add(fractionConfigurationSep);
		profileConfiguration.getFractions().add(fractionConfigurationOct);
		profileConfiguration.getFractions().add(fractionConfigurationNov);
		profileConfiguration.getFractions().add(fractionConfigurationDec);

		profileService.createProfileConfiguration(profileConfiguration);
	}

	@After
	public void afterTests() throws ServiceException {

		profileService.deleteProfileConfiguration("B");
		profileService.deleteProfileConfiguration("A");
		profileService.deleteProfileConfiguration("C");
	}

	@Test
	public void shouldCreateProfileSuccessfully() {
		ProfileConfiguration profileConfiguration = new ProfileConfiguration();

		profileConfiguration.setName("A");

		FractionConfiguration fractionConfigurationJan = new FractionConfiguration(Month.JANUARY, 0.1F);
		FractionConfiguration fractionConfigurationFeb = new FractionConfiguration(Month.FEBRUARY, 0.1F);
		FractionConfiguration fractionConfigurationMar = new FractionConfiguration(Month.MARCH, 0.1F);
		FractionConfiguration fractionConfigurationApr = new FractionConfiguration(Month.APRIL, 0.1F);
		FractionConfiguration fractionConfigurationMay = new FractionConfiguration(Month.MAY, 0.1F);
		FractionConfiguration fractionConfigurationJune = new FractionConfiguration(Month.JUNE, 0.1F);
		FractionConfiguration fractionConfigurationJuly = new FractionConfiguration(Month.JULY, 0.1F);
		FractionConfiguration fractionConfigurationAug = new FractionConfiguration(Month.AUGUST, 0.1F);
		FractionConfiguration fractionConfigurationSep = new FractionConfiguration(Month.SEPTEMBER, 0.05F);
		FractionConfiguration fractionConfigurationOct = new FractionConfiguration(Month.OCTOBER, 0.05F);
		FractionConfiguration fractionConfigurationNov = new FractionConfiguration(Month.NOVEMBER, 0.05F);
		FractionConfiguration fractionConfigurationDec = new FractionConfiguration(Month.DECEMBER, 0.05F);

		profileConfiguration.getFractions().add(fractionConfigurationJan);
		profileConfiguration.getFractions().add(fractionConfigurationFeb);
		profileConfiguration.getFractions().add(fractionConfigurationMar);
		profileConfiguration.getFractions().add(fractionConfigurationApr);
		profileConfiguration.getFractions().add(fractionConfigurationMay);
		profileConfiguration.getFractions().add(fractionConfigurationJune);
		profileConfiguration.getFractions().add(fractionConfigurationJuly);
		profileConfiguration.getFractions().add(fractionConfigurationAug);
		profileConfiguration.getFractions().add(fractionConfigurationSep);
		profileConfiguration.getFractions().add(fractionConfigurationOct);
		profileConfiguration.getFractions().add(fractionConfigurationNov);
		profileConfiguration.getFractions().add(fractionConfigurationDec);

		profileConfiguration = template.postForEntity("/profiles", profileConfiguration, ProfileConfiguration.class)
				.getBody();

		Assert.assertNotNull(profileConfiguration.getProfileId());
		Assert.assertEquals("A", profileConfiguration.getName());
		Assert.assertThat(profileConfiguration.getFractions(),
				CoreMatchers.hasItems(fractionConfigurationJan, fractionConfigurationFeb, fractionConfigurationMar,
						fractionConfigurationApr, fractionConfigurationMay, fractionConfigurationJune,
						fractionConfigurationJuly, fractionConfigurationAug, fractionConfigurationSep,
						fractionConfigurationOct, fractionConfigurationNov, fractionConfigurationDec));

	}

	@Test
	public void shouldReturnAnErrorInfoInCaseOfAnAggregateFractionValueIsMoreThanOne() throws ServiceException {

		ProfileConfiguration profileConfiguration = new ProfileConfiguration();

		profileConfiguration.setName("Z");

		FractionConfiguration fractionConfigurationJan = new FractionConfiguration(Month.JANUARY, 1.001F);
		// FractionConfiguration fractionConfigurationFeb = new
		// FractionConfiguration(Month.FEBRUARY, 0.4F);
		profileConfiguration.getFractions().add(fractionConfigurationJan);

		ErrorInfo error = template.postForEntity("/profiles", profileConfiguration, ErrorInfo.class).getBody();

		Assert.assertEquals("0001", error.getErrorKey());

	}

	@Test
	public void shouldUpdateProfileSuccessfullyInCaseOfValidData() throws ServiceException {

		ProfileConfiguration profileConfiguration = profileService.readProfileConfiguration("B");

		profileConfiguration.setName("C");

		FractionConfiguration fractionConfigurationJan = new FractionConfiguration(Month.JANUARY, 0.1F);
		FractionConfiguration fractionConfigurationFeb = new FractionConfiguration(Month.FEBRUARY, 0.1F);
		FractionConfiguration fractionConfigurationMar = new FractionConfiguration(Month.MARCH, 0.1F);
		FractionConfiguration fractionConfigurationApr = new FractionConfiguration(Month.APRIL, 0.1F);
		FractionConfiguration fractionConfigurationMay = new FractionConfiguration(Month.MAY, 0.1F);
		FractionConfiguration fractionConfigurationJune = new FractionConfiguration(Month.JUNE, 0.1F);
		FractionConfiguration fractionConfigurationJuly = new FractionConfiguration(Month.JULY, 0.1F);
		FractionConfiguration fractionConfigurationAug = new FractionConfiguration(Month.AUGUST, 0.1F);
		FractionConfiguration fractionConfigurationSep = new FractionConfiguration(Month.SEPTEMBER, 0.05F);
		FractionConfiguration fractionConfigurationOct = new FractionConfiguration(Month.OCTOBER, 0.05F);
		FractionConfiguration fractionConfigurationNov = new FractionConfiguration(Month.NOVEMBER, 0.05F);
		FractionConfiguration fractionConfigurationDec = new FractionConfiguration(Month.DECEMBER, 0.05F);

		profileConfiguration.getFractions().clear();

		profileConfiguration.getFractions().add(fractionConfigurationJan);
		profileConfiguration.getFractions().add(fractionConfigurationFeb);
		profileConfiguration.getFractions().add(fractionConfigurationMar);
		profileConfiguration.getFractions().add(fractionConfigurationApr);
		profileConfiguration.getFractions().add(fractionConfigurationMay);
		profileConfiguration.getFractions().add(fractionConfigurationJune);
		profileConfiguration.getFractions().add(fractionConfigurationJuly);
		profileConfiguration.getFractions().add(fractionConfigurationAug);
		profileConfiguration.getFractions().add(fractionConfigurationSep);
		profileConfiguration.getFractions().add(fractionConfigurationOct);
		profileConfiguration.getFractions().add(fractionConfigurationNov);
		profileConfiguration.getFractions().add(fractionConfigurationDec);

		template.put("/profiles/B", profileConfiguration);

		profileConfiguration = template.getForEntity("/profiles/C", ProfileConfiguration.class).getBody();

		Assert.assertNotNull(profileConfiguration.getProfileId());
		Assert.assertEquals("C", profileConfiguration.getName());
		Assert.assertThat(profileConfiguration.getFractions(),
				CoreMatchers.hasItems(fractionConfigurationJan, fractionConfigurationFeb, fractionConfigurationMar,
						fractionConfigurationApr, fractionConfigurationMay, fractionConfigurationJune,
						fractionConfigurationJuly, fractionConfigurationAug, fractionConfigurationSep,
						fractionConfigurationOct, fractionConfigurationNov, fractionConfigurationDec));
		profileConfiguration = profileService.readProfileConfiguration("B");

		Assert.assertNotNull(profileConfiguration);

	}

	@Test
	public void shouldReadProfileSuccessfullyInCaseOfValidName() throws ServiceException {

		ProfileConfiguration profileConfiguration = template.getForEntity("/profiles/B", ProfileConfiguration.class)
				.getBody();

		FractionConfiguration fractionConfigurationJan = new FractionConfiguration(Month.JANUARY, 0.1F);
		FractionConfiguration fractionConfigurationFeb = new FractionConfiguration(Month.FEBRUARY, 0.1F);
		FractionConfiguration fractionConfigurationMar = new FractionConfiguration(Month.MARCH, 0.1F);
		FractionConfiguration fractionConfigurationApr = new FractionConfiguration(Month.APRIL, 0.1F);
		FractionConfiguration fractionConfigurationMay = new FractionConfiguration(Month.MAY, 0.1F);
		FractionConfiguration fractionConfigurationJune = new FractionConfiguration(Month.JUNE, 0.1F);
		FractionConfiguration fractionConfigurationJuly = new FractionConfiguration(Month.JULY, 0.1F);
		FractionConfiguration fractionConfigurationAug = new FractionConfiguration(Month.AUGUST, 0.1F);
		FractionConfiguration fractionConfigurationSep = new FractionConfiguration(Month.SEPTEMBER, 0.1F);
		FractionConfiguration fractionConfigurationOct = new FractionConfiguration(Month.OCTOBER, 0.1F);
		FractionConfiguration fractionConfigurationNov = new FractionConfiguration(Month.NOVEMBER, 0F);
		FractionConfiguration fractionConfigurationDec = new FractionConfiguration(Month.DECEMBER, 0F);

		Assert.assertNotNull(profileConfiguration.getProfileId());
		Assert.assertEquals("B", profileConfiguration.getName());

		Assert.assertThat(profileConfiguration.getFractions(),
				CoreMatchers.hasItems(fractionConfigurationJan, fractionConfigurationFeb, fractionConfigurationMar,
						fractionConfigurationApr, fractionConfigurationMay, fractionConfigurationJune,
						fractionConfigurationJuly, fractionConfigurationAug, fractionConfigurationSep,
						fractionConfigurationOct, fractionConfigurationNov, fractionConfigurationDec));

	}

	@Test
	public void shouldDeleteProfileSuccessfullyInCaseOfValidName() throws ServiceException {

		template.delete("/profiles/B");
		ProfileConfiguration profileConfiguration = profileService.readProfileConfiguration("B");

		Assert.assertNull(profileConfiguration);

	}

}