
package com.mycompany.energyconsumuption.service;

public interface ProfileService {
	/***
	 * Creates given profile configuration along with the profile fractions data for the given months.
	 * @param {@link ProfileConfiguration}
	 * @return {@link ProfileConfiguration}
	 * @throws ServiceException
	 */
	ProfileConfiguration createProfileConfiguration(ProfileConfiguration profileConfiguration) throws AggregateFractionValueIsNotEqualToOneException, ServiceException;
	
	/***
	 * Updates  given profile configuration along with the fractions data for the given months.
	 * @param {@link ProfileConfiguration}
	 * @return {@link ProfileConfiguration}
	 * @throws ServiceException
	 */
	
	ProfileConfiguration updateProfileConfiguration(ProfileConfiguration profileConfiguration) throws AggregateFractionValueIsNotEqualToOneException, ServiceException;
	
	/***
	 * Reads given profile configuration along with the fractions data for the given months.
	 *
	 * @return {@link ProfileConfiguration}
	 * @throws ServiceException
	 */

	ProfileConfiguration readProfileConfiguration(String  profileName) throws ServiceException;

	/***
	 * Deletes given profile configuration along with the fractions data for the given months.
	 *
	 * @return {@link ProfileConfiguration}
	 * @throws ServiceException
	 */

	ProfileConfiguration deleteProfileConfiguration(String  profileName) throws ServiceException;
	
	
}
