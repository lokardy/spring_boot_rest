
package com.mycompany.energyconsumuption.service;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST) 
public class AggregateFractionValueIsNotEqualToOneException extends ServiceException {

	
	public AggregateFractionValueIsNotEqualToOneException() {
		super();
		
	}

	public AggregateFractionValueIsNotEqualToOneException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public AggregateFractionValueIsNotEqualToOneException(String message) {
		super(message);
		
	}

	public AggregateFractionValueIsNotEqualToOneException(Throwable cause) {
		super(cause);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String errorKey = "0001";

	public String getErrorKey() {
		return errorKey;
	}
	
	
}
