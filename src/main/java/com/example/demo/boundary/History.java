package com.example.demo.boundary;

import java.util.Date;

public class History {
	private Date createdTimestamp;
	private Status status;
	private String description;

	public History() {
		super();
	}

	public History(Date createdTimestamp, Status status, String description) {
		super();
		this.createdTimestamp = createdTimestamp;
		this.status = status;
		this.description = description;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "History [createdTimestamp=" + createdTimestamp + ", status=" + status + ", description=" + description
				+ "]";
	}

}
