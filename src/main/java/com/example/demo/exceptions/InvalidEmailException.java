package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidEmailException extends RuntimeException {

	private static final long serialVersionUID = 2931035493302549190L;
	private static final String EMAIL_FORMAT_EXCEPTION = "Email must be in the format of example@example.com";

	public InvalidEmailException() {
		this(EMAIL_FORMAT_EXCEPTION);
	}

	public InvalidEmailException(String message) {
		super(message);
	}

}
