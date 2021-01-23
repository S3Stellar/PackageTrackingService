package com.example.demo.boundary;

import java.util.Date;
import java.util.List;

public class TrackBoundary {

	private String trackId;
	private User user;
	private Order order;
	private String address;
	private Date createdTimestamp;
	private String approximatedArrivalDate;
	private Status status;
	private String description;
	private List<History> history;

	public TrackBoundary() {
		super();
	}

	public TrackBoundary(User user, Order order, String address, String approximatedArrivalDate, Status status,
			String description, List<History> history) {
		super();
		this.user = user;
		this.order = order;
		this.address = address;
		this.approximatedArrivalDate = approximatedArrivalDate;
		this.status = status;
		this.description = description;
		this.history = history;
	}

	public TrackBoundary(String trackId, User user, Order order, String address, Date createdTimestamp,
			String approximatedArrivalDate, Status status, String description, List<History> history) {
		super();
		this.trackId = trackId;
		this.user = user;
		this.order = order;
		this.address = address;
		this.createdTimestamp = createdTimestamp;
		this.approximatedArrivalDate = approximatedArrivalDate;
		this.status = status;
		this.description = description;
		this.history = history;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getApproximatedArrivalDate() {
		return approximatedArrivalDate;
	}

	public void setApproximatedArrivalDate(String approximatedArrivalDate) {
		this.approximatedArrivalDate = approximatedArrivalDate;
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

	public List<History> getHistory() {
		return history;
	}

	public void setHistory(List<History> history) {
		this.history = history;
	}

	@Override
	public String toString() {
		return "TrackBoundary [trackId=" + trackId + ", user=" + user + ", order=" + order + ", address=" + address
				+ ", createdTimestamp=" + createdTimestamp + ", approximatedArrivalDate=" + approximatedArrivalDate
				+ ", status=" + status + ", description=" + description + ", history=" + history + "]";
	}

}
