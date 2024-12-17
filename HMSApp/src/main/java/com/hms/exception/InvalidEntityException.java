package com.hms.exception;

import org.springframework.http.HttpStatus;

public class InvalidEntityException extends Exception {
	private String errorMessage;
    private HttpStatus httpStatus;


	public InvalidEntityException(String msg) {
		super(msg);
	}
	 public InvalidEntityException(String msg, HttpStatus httpStatus) {
	        super(msg);
	        this.errorMessage = msg;
	        this.httpStatus = httpStatus;
	    }
}
