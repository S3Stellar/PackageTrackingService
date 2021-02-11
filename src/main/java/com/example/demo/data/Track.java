package com.example.demo.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.boundary.History;
import com.example.demo.boundary.Order;
import com.example.demo.boundary.Status;
import com.example.demo.boundary.User;

@Document(collection = "Tracks")
public class Track {

	@Id
	private String trackId;
	private User user;
	private Order order;
	private String address;
	private Date createdTimestamp;
	private Date approximatedArrivalDate;
	private Status status;
	private String description;
	private List<History> history;

	public Track() {
		super();
	}

	public Track(User user, Order order, String address, Date createdTimestamp, Date approximatedArrivalDate,
			Status status, String description, List<History> history) {
		super();
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

	public Date getApproximatedArrivalDate() {
		return approximatedArrivalDate;
	}

	public void setApproximatedArrivalDate(Date approximatedArrivalDate) {
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
