
package com.mycompany.energyconsumuption.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND) 
public class ProfileNotFoundException extends ServiceException {
	
	
	public ProfileNotFoundException() {
		super();
	
	}

	public ProfileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public ProfileNotFoundException(String message) {
		super(message);
		
	}

	public ProfileNotFoundException(Throwable cause) {
		super(cause);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String errorKey = "0002";

	public String getErrorKey() {
		return errorKey;
	}
}
