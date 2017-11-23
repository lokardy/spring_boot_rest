
package com.mycompany.energyconsumption.entity.meter;

import java.time.Month;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "METER_READINGS", schema ="POWERHOUSE")
public class MeterReading {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="meter_reading_id")
	private Long meterId;
	
	@Column(name="month_id")
	private Month month;
	
	@Column(name="value")
	private Integer value;
	
	@ManyToOne
	@JoinColumn(name = "meter_id")
	private Meter meter;

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Meter getMeter() {
		return meter;
	}

	public void setMeter(Meter meter) {
		this.meter = meter;
	}
	
	
}
