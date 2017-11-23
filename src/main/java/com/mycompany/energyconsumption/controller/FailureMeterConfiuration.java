package com.mycompany.energyconsumption.controller;

import com.mycompany.energyconsumuption.service.MeterConfiguration;

public class FailureMeterConfiuration {

	private String failedReason;
	
	private MeterConfiguration meterConfiguration;

	public FailureMeterConfiuration() {
		super();
	}

	public FailureMeterConfiuration(String failedReason, MeterConfiguration meterConfiguration) {
		super();
		this.failedReason = failedReason;
		this.meterConfiguration = meterConfiguration;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public MeterConfiguration getMeterConfiguration() {
		return meterConfiguration;
	}
	
	
	
}
