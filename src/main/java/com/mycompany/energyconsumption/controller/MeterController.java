package com.mycompany.energyconsumption.controller;

import java.time.Month;
import java.util.List;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.energyconsumuption.service.MeterConfiguration;
import com.mycompany.energyconsumuption.service.MeterService;
import com.mycompany.energyconsumuption.service.PowerConsumption;
import com.mycompany.energyconsumuption.service.ServiceException;

@Controller
public class MeterController {

	@Autowired
	private MeterService meterService;

	@PostMapping(value = "/meters")
	public ResponseEntity<MeterConfiguration> createMeter(@RequestBody MeterConfiguration meterConfiguration)
			throws ServiceException {

		MeterConfiguration returnMeterConfiguration = meterService.createMeterConfiguration(meterConfiguration);

		return new ResponseEntity<MeterConfiguration>(returnMeterConfiguration, HttpStatus.OK);
	}
	
	//Adding only one end point for demo purpose. Others would be in similar lines
	@PostMapping(value = "/meters-collection")
	public ResponseEntity<BulkMeterOperationResponse> createMeterBulk(@RequestBody List<MeterConfiguration> meterConfigurations)
			throws ServiceException {

		int successCount = 0;
		int failureCount = 0;
		
		BulkMeterOperationResponse bulkMeterOperationResponse = new BulkMeterOperationResponse();
		
		for(MeterConfiguration mc : meterConfigurations) {
			
			try {
			MeterConfiguration returnMeterConfiguration = meterService.createMeterConfiguration(mc);
			successCount++;
			bulkMeterOperationResponse.getSuccessProfiles().add(returnMeterConfiguration);
			} catch(Exception e) {
				failureCount++;
				bulkMeterOperationResponse.getFailureProfiles().add(new FailureMeterConfiuration(e.getMessage(), mc));
			}

		}
		
		bulkMeterOperationResponse.setFailureCount(failureCount);
		bulkMeterOperationResponse.setSuccessCount(successCount);
		
		return new ResponseEntity<BulkMeterOperationResponse>(bulkMeterOperationResponse, HttpStatus.OK);
		
	}


	@PutMapping(value = "/meters/{meterId}")
	public ResponseEntity<MeterConfiguration> updateMeter(@PathVariable String meterId,
			@RequestBody MeterConfiguration meterConfiguration) throws ServiceException {

		if (meterService.readMeterConfiguration(meterId) == null) {
			return new ResponseEntity("No Meter found for ID " + meterId, HttpStatus.NOT_FOUND);
		}

		MeterConfiguration returnMeterConfiguration = meterService.createMeterConfiguration(meterConfiguration);

		return new ResponseEntity<MeterConfiguration>(returnMeterConfiguration, HttpStatus.OK);
	}

	@DeleteMapping("/meters/{meterId}")
	public ResponseEntity<MeterConfiguration> deleteMeter(@PathVariable String meterId) throws ServiceException {

		if (meterService.readMeterConfiguration(meterId) == null) {
			return new ResponseEntity("No Meter found for ID " + meterId, HttpStatus.NOT_FOUND);
		}

		MeterConfiguration returnMeterConfiguration = meterService.deleteMeterConfiguration(meterId);

		return new ResponseEntity<MeterConfiguration>(returnMeterConfiguration, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/meters/{meterId}", params = { "columns", "month"},  method = RequestMethod.GET)
	public ResponseEntity<MeterFinderMethodResponse> findPowerConsumption(@PathVariable("meterId") String meterId, @RequestParam("columns") String columns, @RequestParam("month") Month month) throws ServiceException {

		// Skipping columns for now since we have only power consumption to be calculated. But as and when we need to support more such columns we need to parse the string and call appropriate methods from the service.

		PowerConsumption powerConsumption = meterService.findPowerConsumptionForTheGivenMeterIdAndMonth(meterId, month);
		
		if (powerConsumption == null) {
			return new ResponseEntity("No Meter found for ID " + meterId, HttpStatus.NOT_FOUND);
		}

		MeterFinderMethodResponse meterFinderMethodResponse = new MeterFinderMethodResponse();
		
		meterFinderMethodResponse.setMeterId(meterId);
		meterFinderMethodResponse.setMonth(month);
		meterFinderMethodResponse.setPowerConsumption(powerConsumption.getConsumption());
		
		return new ResponseEntity<MeterFinderMethodResponse>(meterFinderMethodResponse, HttpStatus.OK);

	}

	@GetMapping("/meters/{meterId}")
	public ResponseEntity<MeterConfiguration> getMeter(@PathVariable String meterId) throws ServiceException {
		MeterConfiguration returnMeterConfiguration = meterService.readMeterConfiguration(meterId);

		if (returnMeterConfiguration == null) {
			return new ResponseEntity("No Meter found for ID " + meterId, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<MeterConfiguration>(returnMeterConfiguration, HttpStatus.OK);

	}

}
