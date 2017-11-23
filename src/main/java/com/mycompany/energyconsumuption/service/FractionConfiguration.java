
package com.mycompany.energyconsumuption.service;

import java.time.Month;

public class FractionConfiguration {

	
	public FractionConfiguration() {
		super();
	}

	public FractionConfiguration(Month month, Float value) {
		super();
		this.month = month;
		this.value = value;
	}


	private Month month;
	
	private Float value;

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FractionConfiguration other = (FractionConfiguration) obj;
		if (month != other.month)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	

}
