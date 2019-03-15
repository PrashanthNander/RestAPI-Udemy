package com.prash.ws.reponse;

public enum ErrorMessages {
	
	MISSING_REQUIRED_FIELDS("Missing required fields. Check documention for more details"),
	RECORD_ALREADY_EXISTS("Record already exists"),
	INTERNAL_SERVER_ERROR("Internal Server error"),
	NO_RECORD_FOUND("Record not found"),
	AUTHENTIFICATION_FAILED("Authentification failed"),
	COULD_NOT_UPDATE_RECORD("Could not update record"),
	COULD_NOT_DELETE_RECORD("Could not delete record"),
	EMAIL_ADDRESS_NOT_VERIFIED("Email address could not verified");
	
	private String errorMessage;
	
	ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
