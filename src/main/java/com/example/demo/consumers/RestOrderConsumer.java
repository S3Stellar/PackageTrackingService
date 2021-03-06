package com.example.demo.consumers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.exceptions.NoSuchUserException;

@Component
public class RestOrderConsumer implements RestConsumer<String, OrderResponse>{
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
	public OrderResponse fetch(String key) {
		ResponseEntity<OrderResponse> response;

		try {
			response = restTemplate.getForEntity(url + "/shoppingCarts/{shoppingCartId}", OrderResponse.class, key);
		} catch (Exception e) {
			throw new NoSuchUserException("Couldn't fetch order: " + key);
		}

		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody();
		} else {
			throw new NoSuchUserException("Couldn't fetch order: " + key);
		}		
	}
	


}
