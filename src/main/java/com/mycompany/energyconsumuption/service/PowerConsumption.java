package com.mycompany.energyconsumuption.service;

import java.time.Month;

public class PowerConsumption {

	private Integer consumption;
	
	private String meterId;
	
	private Month month;
	
	public PowerConsumption() {
		super();
	}

	public PowerConsumption(Integer consumption, String meterId, Month month) {
		super();
		this.consumption = consumption;
		this.meterId = meterId;
		this.month = month;
	}

	public Integer getConsumption() {
		return consumption;
	}

	public void setConsumption(Integer consumption) {
		this.consumption = consumption;
	}

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}
	
	
}
