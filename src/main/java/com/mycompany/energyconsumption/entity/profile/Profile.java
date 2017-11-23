
package com.mycompany.energyconsumption.entity.profile;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "PROFILE", schema ="POWERHOUSE")
public class Profile {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="profile_id")
	private Long profileId;
	
	@Column(name="name", nullable = false,  length = 256, unique=true)
	private String name;
	

	@OneToMany(cascade = {CascadeType.ALL}, fetch =  FetchType.LAZY, mappedBy = "profile")
	private List<Fraction> fractions;

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public List<Fraction> getFractions() {
		if(fractions == null) {
			this.fractions = new ArrayList<>(); 
		}
		
		return fractions;
	}

	public void setFractions(List<Fraction> fractions) {
		this.fractions = fractions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
