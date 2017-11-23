package com.mycompany.energyconsumption.controller;

import java.time.Month;

public class MeterFinderMethodResponse {

	private Integer powerConsumption;
	
	private Month month;
	
	private String meterId;

	public Integer getPowerConsumption() {
		return powerConsumption;
	}

	public void setPowerConsumption(Integer powerConsumption) {
		this.powerConsumption = powerConsumption;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	
	
	
}
