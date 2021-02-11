package com.example.demo.consumers;

import com.example.demo.boundary.Order;
import com.example.demo.boundary.User;

public class OrderResponse {

	private User user;
	private Order order;

	public OrderResponse() {

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

}
