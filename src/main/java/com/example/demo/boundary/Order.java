package com.example.demo.boundary;

public class Order {

	private String shoppingCartId;
	private Boolean expired;

	public Order() {
		super();
	}

	public Order(String shoppingCartId, Boolean expired) {
		super();
		this.shoppingCartId = shoppingCartId;
		this.expired = expired;
	}

	public String getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(String shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	@Override
	public String toString() {
		return "Order [shoppingCartId=" + shoppingCartId + ", expired=" + expired + "]";
	}

}
