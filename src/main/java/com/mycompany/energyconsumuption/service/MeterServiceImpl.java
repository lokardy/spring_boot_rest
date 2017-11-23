
package com.mycompany.energyconsumuption.service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.energyconsumption.entity.meter.Meter;
import com.mycompany.energyconsumption.entity.meter.MeterReading;
import com.mycompany.energyconsumption.entity.profile.Fraction;
import com.mycompany.energyconsumption.entity.profile.Profile;
import com.mycompany.energyconsumption.repository.MeterRepository;
import com.mycompany.energyconsumption.repository.ProfileRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MeterServiceImpl implements MeterService {

	@Autowired
	private MeterRepository meterRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public MeterConfiguration createMeterConfiguration(MeterConfiguration meterConfiguration)
			throws ProfileNotFoundException, CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException,
			InvalidConsumptionException, ServiceException {
		
		Profile profile = profileRepository.findByName(meterConfiguration.getProfileName());

		validateMeterConfiguration(meterConfiguration, profile);

		// In large scale projects we can use converts like Orika or dozer.

		// Move these converts to separate hierarchy if they are reusable
		Meter meter = convertToMeter(meterConfiguration, profile);

		Meter returnMeter = meterRepository.save(meter);

		return convertToMeterConfiguration(returnMeter);
	}

	

	@Override
	public MeterConfiguration updateMeterConfiguration(MeterConfiguration meterConfiguration)
			throws ProfileNotFoundException, CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException,
			InvalidConsumptionException, ServiceException {

		Profile profile = profileRepository.findByName(meterConfiguration.getProfileName());

		validateMeterConfiguration(meterConfiguration, profile);

		Meter meter = convertToMeter(meterConfiguration, profile);

		Meter returnMeter = meterRepository.save(meter);

		return convertToMeterConfiguration(returnMeter);
	}

	@Override
	public MeterConfiguration readMeterConfiguration(String meterId) throws ServiceException {
		Meter meter = meterRepository.findOne(meterId);

		if(meter == null) {
			return null;
		}
		
		return convertToMeterConfiguration(meter);
	}

	@Override
	public MeterConfiguration deleteMeterConfiguration(String meterId) throws ServiceException {
		Meter meter = meterRepository.findOne(meterId);

		if (meter == null) {
			return null;
		}

		meterRepository.delete(meter);

		return convertToMeterConfiguration(meter);
	}

	private Meter convertToMeter(MeterConfiguration meterConfiguration, Profile profile) {
		final Meter meter = new Meter();

		meter.setMeterId(meterConfiguration.getMeterId());

		// Profile profile =
		// profileRepository.findByName(meterConfiguration.getProfileName());

		meter.setProfile(profile);

		meterConfiguration.getMeterReadings().forEach(item -> {

			MeterReading meterReading = new MeterReading();

			meterReading.setMeter(meter);
			meterReading.setMonth(item.getMonth());
			meterReading.setValue(item.getValue());

			meter.getMeterReadings().add(meterReading);

		});
		return meter;

	}

	private MeterConfiguration convertToMeterConfiguration(Meter returnMeter) {
		MeterConfiguration returnConfiguration = new MeterConfiguration();

		returnConfiguration.setMeterId(returnMeter.getMeterId());
		returnConfiguration.setProfileName(returnMeter.getProfile().getName());

		returnMeter.getMeterReadings().forEach(item -> {

			MeterReadingConfiguration meterReading = new MeterReadingConfiguration();

			meterReading.setMonth(item.getMonth());
			meterReading.setValue(item.getValue());

			returnConfiguration.getMeterReadings().add(meterReading);

		});
		return returnConfiguration;
	}
	
	@Override
	public PowerConsumption findPowerConsumptionForTheGivenMeterIdAndMonth(String meterId, Month month)
			throws ServiceException {
		Meter meter = meterRepository.findOne(meterId);

		if(meter == null) {
			return null;
		}
		
		//These values can be cached if they are not going to change once entered either through JPA level 2 cache or explicit caching
		MeterReading[] meterConfigs = new MeterReading[12];
		
		meter.getMeterReadings().forEach(item -> {
			meterConfigs[item.getMonth().getValue() -1] = item;
		});
	
		int consumption = meterConfigs[month.getValue() - 1].getValue()  - meterConfigs[month.getValue() - 2].getValue();
		
		return new PowerConsumption(consumption, meterId, month);
	}
	
	
	private void validateMeterConfiguration(MeterConfiguration meterConfiguration, Profile profile)
			throws ProfileNotFoundException, CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException,
			InvalidConsumptionException {

		if (profile == null) {
			throw new ProfileNotFoundException("Profile Not found " + meterConfiguration.getProfileName());
		}

		List<MeterReadingConfiguration> meterReadings = meterConfiguration.getMeterReadings();

		Collections.sort(meterReadings, (o1, o2) -> {

			if (o1.getMonth().compareTo(o2.getMonth()) > 0) {
				return 1;
			} else if (o1.getMonth().compareTo(o2.getMonth()) < 0) {
				return -1;
			}
			return 0;
		});

		Map<Month, Float> fractionValues = profile.getFractions().stream()
				.collect(Collectors.toMap(Fraction::getMonth, Fraction::getValue));

		MeterReadingConfiguration previousMonthMeterReading = meterReadings.iterator().next();

		int sumOfMeterReadings = meterReadings.stream().mapToInt(item -> item.getValue()).sum();

		for (int index = 1; index < meterReadings.size(); index++) {

			MeterReadingConfiguration currentMonthMeterReadingValue = meterReadings.get(index);

			if (currentMonthMeterReadingValue.getValue() < previousMonthMeterReading.getValue()) {

				throw new CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException(String.format(
						"The meter reading value of %d for the month %s is less than the previous month[%s] value %d",
						currentMonthMeterReadingValue.getValue(), currentMonthMeterReadingValue.getMonth(),
						previousMonthMeterReading.getMonth(), previousMonthMeterReading.getValue()));
			}

			BigDecimal consumption = new BigDecimal(
					currentMonthMeterReadingValue.getValue() - previousMonthMeterReading.getValue());

			// Round off and then convert to integer
			Float allowedMeanConsumption = sumOfMeterReadings
					* fractionValues.get(currentMonthMeterReadingValue.getMonth());
			BigDecimal allowedMinConsumption = new BigDecimal(
					allowedMeanConsumption - (allowedMeanConsumption * 0.25F));
			BigDecimal allowedMaxConsumption = new BigDecimal(
					allowedMeanConsumption + (allowedMeanConsumption * 0.25F));

			consumption = consumption.setScale(4, BigDecimal.ROUND_DOWN);
			allowedMinConsumption = allowedMinConsumption.setScale(4, BigDecimal.ROUND_DOWN);
			allowedMaxConsumption = allowedMaxConsumption.setScale(4, BigDecimal.ROUND_DOWN);

			if (consumption.compareTo(allowedMinConsumption) < 0 || consumption.compareTo(allowedMaxConsumption) > 0) {
				throw new InvalidConsumptionException(String.format(
						"Invalid consumption for the month %s,The actual consumption is %f but it must be between %f and %f",
						currentMonthMeterReadingValue.getMonth(), consumption.doubleValue(),
						allowedMinConsumption.doubleValue(), allowedMaxConsumption.doubleValue()));
			}

			previousMonthMeterReading = currentMonthMeterReadingValue;
		}
		
	}


}
