
package com.mycompany.energyconsumuption.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mycompany.energyconsumuption.service.ServiceException;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR) 
public class InvalidConsumptionException extends ServiceException {
	
	
	public InvalidConsumptionException() {
		super();
		
	}

	public InvalidConsumptionException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public InvalidConsumptionException(String message) {
		super(message);
		
	}

	public InvalidConsumptionException(Throwable cause) {
		super(cause);
	
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String errorKey = "0005";

	public String getErrorKey() {
		return errorKey;
	}
	
}
