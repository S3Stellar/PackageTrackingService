package com.example.demo.boundary;

public enum Status {
	DEPARTED("DEPARTED"), ARRIVED("ARRIVED"), ACCEPTED("ACCEPTED"), LOST("LOST");

	private String value;

	public String getValue() {
		return value;
	}

	 Status(String value) {
		this.value = value;
	}
}
