
package com.mycompany.energyconsumuption.service;

import java.time.Month;

// If we need to ship interfaces across the modules then the interfaces can be separated out into separate module. Infact we can have dedicated module for service, repository and entities.
public interface MeterService {

	/***
	 * Creates given meter configuration along with the meter reading data for the given months.
	 * @param {@link MeterConfiguration}
	 * @return {@link MeterConfiguration}
	 * @throws ServiceException
	 */
	MeterConfiguration createMeterConfiguration(MeterConfiguration meterConfiguration) throws ServiceException;
	
	/***
	 * Updates  given meter configuration along with the meter reading data for the given months.
	 * @param {@link MeterConfiguration}
	 * @return {@link MeterConfiguration}
	 * @throws ServiceException
	 */
	
	MeterConfiguration updateMeterConfiguration(MeterConfiguration meterConfiguration) throws ServiceException;
	
	/***
	 * Reads given meter configuration along with the meter reading data for the given months.
	 * 
	 * @return {@link MeterConfiguration}
	 * @throws ServiceException
	 */

	MeterConfiguration readMeterConfiguration(String  meterId) throws ServiceException;

	/***
	 * Deletes given meter configuration along with the meter reading data for the given months.
	 * 
	 * @return {@link MeterConfiguration}
	 * @throws ServiceException
	 */

	MeterConfiguration deleteMeterConfiguration(String  meterId) throws ServiceException;
	
	/**
	 * Find the power consumption for the given meter Id and month.
	 * @param meterId
	 * @param month
	 * @return {@link PowerConsumption}
	 * @throws ServiceException
	 */
	PowerConsumption findPowerConsumptionForTheGivenMeterIdAndMonth(String meterId, Month month) throws ServiceException;
	
	
}
