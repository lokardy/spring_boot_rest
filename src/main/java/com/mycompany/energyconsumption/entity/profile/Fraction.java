
package com.mycompany.energyconsumption.entity.profile;

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
@Table(name = "PROFILE_FRACTION", schema ="POWERHOUSE")
public class Fraction {

	
	public Fraction( Month month, Float value, Profile profile) {
		super();
	
		this.month = month;
		this.value = value;
		this.profile = profile;
	}

	public Fraction() {
		super();
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="fraction_id")
	private Long fractionId;
	
	@Column(name="month_id")
	private Month month;
	
	@Column(name="value")
	private Float value;
	
	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;
	

	public Long getFractionId() {
		return fractionId;
	}

	public void setFractionId(Long fractionId) {
		this.fractionId = fractionId;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

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
	
	
	
	
}
