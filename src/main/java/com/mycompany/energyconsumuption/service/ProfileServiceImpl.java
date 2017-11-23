
package com.mycompany.energyconsumuption.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.energyconsumption.entity.profile.Fraction;
import com.mycompany.energyconsumption.entity.profile.Profile;
import com.mycompany.energyconsumption.repository.ProfileRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ProfileServiceImpl implements ProfileService {
	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public ProfileConfiguration createProfileConfiguration(ProfileConfiguration profileConfiguration)
			throws ServiceException {

		validateProfile(profileConfiguration);

		// COnverting from one from to other form can be separeted out into different
		// hierarchy in large projects. Have a generic convertor which converts from one
		// form to other.
		Profile profile = convertToProfile(profileConfiguration);

		Profile returnProfile = profileRepository.save(profile);

		return convertToProfileConfiguration(returnProfile);
	}

	@Override
	public ProfileConfiguration updateProfileConfiguration(ProfileConfiguration profileConfiguration)
			throws ServiceException {

		validateProfile(profileConfiguration);

		// COnverting from one from to other form can be separeted out into different
		// hierarchy in large projects. Have a generic converter which converts from one
		// form to other.
		Profile profile = convertToProfile(profileConfiguration);

		Profile returnProfile = profileRepository.save(profile);

		return convertToProfileConfiguration(returnProfile);
	}

	@Override
	public ProfileConfiguration readProfileConfiguration(String profileName) throws ServiceException {

		Profile profile = profileRepository.findByName(profileName);

		if (profile == null) {
			return null;
		}

		return convertToProfileConfiguration(profile);
	}

	@Override
	public ProfileConfiguration deleteProfileConfiguration(String profileName) throws ServiceException {
		Profile profile = profileRepository.findByName(profileName);

		if (profile == null) {
			return null;
		}
		ProfileConfiguration profileConfiguration = convertToProfileConfiguration(profile);

		profileRepository.delete(profile);

		return profileConfiguration;
	}

	private void validateProfile(ProfileConfiguration profileConfiguration)
			throws AggregateFractionValueIsNotEqualToOneException {
		Float fractionSum = 0F;
		BigDecimal  compareValue = new BigDecimal(1.0000F);

		for (FractionConfiguration fractionConfiguration : profileConfiguration.getFractions()) {

			fractionSum = Float.sum(fractionSum, fractionConfiguration.getValue());

		}

		BigDecimal fractionSumAsBigDecimal = new BigDecimal(fractionSum);
		
		fractionSumAsBigDecimal = fractionSumAsBigDecimal.setScale(4, BigDecimal.ROUND_DOWN);
		compareValue = compareValue.setScale(4, BigDecimal.ROUND_DOWN);
		
		//Compare upto 4 digits.
		
		if (!fractionSumAsBigDecimal.equals(compareValue) ) {
			throw new AggregateFractionValueIsNotEqualToOneException(String.format(
					"Fraction values for the given profile %s   is not equal 1", profileConfiguration.getName()));
		}

	}

	private ProfileConfiguration convertToProfileConfiguration(Profile returnProfile) {
		ProfileConfiguration returnProfileConfiguration = new ProfileConfiguration();

		returnProfileConfiguration.setProfileId(returnProfile.getProfileId());
		returnProfileConfiguration.setName(returnProfile.getName());
		returnProfileConfiguration.setProfileId(returnProfile.getProfileId());

		returnProfile.getFractions().forEach(item -> {

			FractionConfiguration fractionConfiguration = new FractionConfiguration();

			fractionConfiguration.setMonth(item.getMonth());
			fractionConfiguration.setValue(item.getValue());

			returnProfileConfiguration.getFractions().add(fractionConfiguration);

		});

		return returnProfileConfiguration;

	}

	private Profile convertToProfile(ProfileConfiguration profileConfiguration) {
		Profile profile = new Profile();

		profile.setName(profileConfiguration.getName());

		if (profileConfiguration.getFractions() != null) {

			profileConfiguration.getFractions().forEach(item -> {

				Fraction fraction = new Fraction();

				fraction.setMonth(item.getMonth());
				fraction.setValue(item.getValue());
				fraction.setProfile(profile);

				profile.getFractions().add(fraction);

			});
		}

		return profile;
	}

}
