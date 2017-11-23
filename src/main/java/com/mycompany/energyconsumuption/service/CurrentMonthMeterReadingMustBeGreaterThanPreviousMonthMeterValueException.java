
package com.mycompany.energyconsumuption.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST) 
public class CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException extends ServiceException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	public CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CurrentMonthMeterReadingMustBeGreaterThanPreviousMonthMeterValueException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	private String errorKey = "0003";

	public String getErrorKey() {
		return errorKey;
	}
}
