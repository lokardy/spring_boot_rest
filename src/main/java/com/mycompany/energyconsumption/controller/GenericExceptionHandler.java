package com.mycompany.energyconsumption.controller;


import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mycompany.energyconsumuption.service.ServiceException;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //For now defaulting to 500 errors. We need logic here to send appropriate error code 
  public    @ResponseBody ErrorInfo handleConflict(Exception ex, WebRequest request) {
    	ErrorInfo error = new ErrorInfo();
    	if(ex instanceof ServiceException) {
	
    		
    		error.setErrorKey(((ServiceException) ex).getErrorKey());
    	
    	}
    	
    	error.setMessage(ex.getMessage());
    	
      return error;
    }
}