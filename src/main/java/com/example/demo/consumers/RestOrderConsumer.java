package com.example.demo.consumers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.boundary.Order;
import com.example.demo.exceptions.FailedToFindUserException;

@Component
public class RestOrderConsumer implements RestConsumer<String, Order>{
	private RestTemplate restTemplate;
	private int port;
	private String host;
	private String url;

	@Value(value = "${orderHost:localhost}")
	public void setHost(String host) {
		this.host = host;
	}

	@Value(value = "${orderPort:8083}")
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void setup() {
		this.restTemplate = new RestTemplate();
		url = "http://" + host + ":" + port;
	}


	@Override
	public Order fetch(String key) {
		ResponseEntity<Order> response;

		try {
			response = restTemplate.getForEntity(url + "/shoppingCarts/{shoppingCartId}", Order.class, key);
		} catch (Exception e) {
			throw new FailedToFindUserException("Couldn't fetch user: " + key);
		}

		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody();
		} else {
			throw new FailedToFindUserException("Couldn't fetch user: " + key);
		}		
	}


}
