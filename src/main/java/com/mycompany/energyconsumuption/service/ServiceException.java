
package com.mycompany.energyconsumuption.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR) 
public class ServiceException extends Exception {

	public ServiceException() {
		super();
		
	}


	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public ServiceException(String message) {
		super(message);
		
	}

	public ServiceException(Throwable cause) {
		super(cause);
	
	}
	
	public String getErrorKey() {
		return null;
	}
	

}
