
package com.mycompany.energyconsumuption.service;

import java.util.ArrayList;
import java.util.List;
/**
 * Meter data to be provided by the clients
 * @author lokesh
 *
 */
public class MeterConfiguration {

	/**
	 * Meter Id.Must be unique
	 */
	private String meterId;
	
	/**
	 * The Associated profile name. profile to meterId mapping must be unique.
	 */
	private String profileName;
	
	/** 
	 * Readings for the given meter for 12 months.
	 */
	private List<MeterReadingConfiguration> meterReadings;
	

	public List<MeterReadingConfiguration> getMeterReadings() {
		
		if(meterReadings == null) {
			meterReadings = new ArrayList<>();
		}
		
		return meterReadings;
	}

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

}
