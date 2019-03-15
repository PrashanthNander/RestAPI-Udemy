package com.prash.ws.exception;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 1038660973588709612L;
	
	public UserServiceException(String message) {
		super(message);
	}

}
