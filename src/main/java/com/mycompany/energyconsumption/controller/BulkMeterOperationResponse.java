package com.mycompany.energyconsumption.controller;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.energyconsumuption.service.MeterConfiguration;

public class BulkMeterOperationResponse {

	private int successCount;
	
	private int failureCount;
	
	private List<MeterConfiguration> successProfiles;
	
	private List<FailureMeterConfiuration> failureProfiles;

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public List<FailureMeterConfiuration> getFailureProfiles() {
		if(failureProfiles == null) {
			failureProfiles = new ArrayList<>();
		}
		
		return failureProfiles;
	}



	public int getSuccessCount() {
		return successCount;
	}

	public List<MeterConfiguration> getSuccessProfiles() {
		if(successProfiles == null) {
			successProfiles = new ArrayList<>();
		}
		
		return successProfiles;
	}
	
	
	
	
}
