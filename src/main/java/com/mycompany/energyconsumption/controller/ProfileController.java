package com.mycompany.energyconsumption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycompany.energyconsumuption.service.MeterConfiguration;
import com.mycompany.energyconsumuption.service.ProfileConfiguration;
import com.mycompany.energyconsumuption.service.ProfileService;
import com.mycompany.energyconsumuption.service.ServiceException;

@Controller
public class ProfileController  {

	@Autowired
	private ProfileService  profileService;
	
	@PostMapping(value = "/profiles")
	public ResponseEntity<ProfileConfiguration> createProfile(@RequestBody ProfileConfiguration profileConfiguration) throws Exception {
		profileConfiguration =	profileService.createProfileConfiguration(profileConfiguration);
		return new ResponseEntity<ProfileConfiguration>(profileConfiguration, HttpStatus.OK);
	}
	
	
	@PutMapping(value = "/profiles/{profileName}")
	public ResponseEntity<ProfileConfiguration> updateMeter(@PathVariable String profileName,
			@RequestBody ProfileConfiguration profileConfiguration) throws ServiceException {

		if (profileService.readProfileConfiguration(profileName) == null) {
			return new ResponseEntity("No profile found for the given name  " + profileName, HttpStatus.NOT_FOUND);
		}

		 profileConfiguration = profileService.updateProfileConfiguration(profileConfiguration);

		return new ResponseEntity<ProfileConfiguration>(profileConfiguration, HttpStatus.OK);
	}

	@DeleteMapping("/profiles/{profileName}")
	public ResponseEntity<ProfileConfiguration> deleteMeter(@PathVariable String profileName) throws ServiceException {

		if (profileService.readProfileConfiguration(profileName) == null) {
			return new ResponseEntity("No profile found for the given name  " + profileName, HttpStatus.NOT_FOUND);
		}

		ProfileConfiguration profileConfiguration = profileService.deleteProfileConfiguration(profileName);

		return new ResponseEntity<ProfileConfiguration>(profileConfiguration, HttpStatus.OK);

	}
	
	@GetMapping("/profiles/{profileName}")
	public ResponseEntity<ProfileConfiguration> getProfile(@PathVariable String profileName) throws ServiceException {
		ProfileConfiguration profileConfiguration = profileService.readProfileConfiguration(profileName);

		if (profileConfiguration == null) {
			return new ResponseEntity("No profile found for the given name  " + profileName, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<ProfileConfiguration>(profileConfiguration, HttpStatus.OK);

	}
	
	
}
