package com.prash.ws.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {
	
	@ExceptionHandler(value= {UserServiceException.class})
	public ResponseEntity<Object> handleServiceException(UserServiceException ex, WebRequest request) {
		CustomExceptionResponse customResponse = new CustomExceptionResponse(new Date(), ex.getMessage());
		return new ResponseEntity<>(customResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleOtherException(UserServiceException ex, WebRequest request) {
		CustomExceptionResponse customResponse = new CustomExceptionResponse(new Date(), ex.getMessage());
		return new ResponseEntity<>(customResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
