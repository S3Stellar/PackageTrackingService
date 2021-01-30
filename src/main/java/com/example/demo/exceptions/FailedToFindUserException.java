package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedToFindUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FailedToFindUserException(String message) {
		super(message);
	}

	public FailedToFindUserException() {
	}

}