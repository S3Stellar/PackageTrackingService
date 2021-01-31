package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TrackNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TrackNotFoundException(String message) {
		super(message);
	}

	public TrackNotFoundException() {
	}

}
