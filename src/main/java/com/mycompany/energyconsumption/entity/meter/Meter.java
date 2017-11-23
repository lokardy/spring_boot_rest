
package com.mycompany.energyconsumption.entity.meter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mycompany.energyconsumption.entity.profile.Profile;

@Entity
@Table(name = "METER", schema = "POWERHOUSE")
public class Meter {
	@Id
	@Column(name = "meter_id")
	private String meterId;

	//Assumed one to one mapping but seems it isn't as  i read more about problem but keeping as is due to time constraint.
	@OneToOne(cascade = { CascadeType.REFRESH })
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "meter")

	private List<MeterReading> meterReadings;

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public List<MeterReading> getMeterReadings() {
		if (meterReadings == null) {
			this.meterReadings = new ArrayList<>();
		}

		return meterReadings;
	}

	public void setMeterReadings(List<MeterReading> meterReadings) {
		this.meterReadings = meterReadings;
	}

}
