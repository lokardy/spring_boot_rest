
package com.mycompany.energyconsumuption.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile configuration data
 * @author lokesh
 *
 */
public class ProfileConfiguration {

	/**
	 * Unique profile Id.
	 */
	private Long profileId;
	
	/**
	 * Name of the profile
	 */
	private String name;

	/**
	 * Fractions for the profile. the sum of all the twelve months should be 1
	 */
	private List<FractionConfiguration> fractions;
	
	
	
	
	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}


	public List<FractionConfiguration> getFractions() {
		if(fractions == null) {
			this.fractions = new ArrayList<>();
		}
		
		return fractions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
