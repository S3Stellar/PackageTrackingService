package com.example.demo.consumers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.boundary.User;
import com.example.demo.exceptions.NoSuchUserException;

@Component
public class RestUserConsumer implements RestConsumer<String, User> {
	private RestTemplate restTemplate;
	private int port;
	private String host;
	private String url;

	@Value(value = "${userHost:localhost}")
	public void setHost(String host) {
		this.host = host;
	}

	@Value(value = "${userPort:8080}")
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void setup() {
		this.restTemplate = new RestTemplate();
		url = "http://" + host + ":" + port;
	}

	@Override
	public User fetch(String key) {
		ResponseEntity<User> response;

		try {
			response = restTemplate.getForEntity(url + "/users/{email}", User.class, key);
		} catch (Exception e) {
			throw new NoSuchUserException("Couldn't fetch user: " + key);
		}

		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody();
		} else {
			throw new NoSuchUserException("Couldn't fetch user: " + key);
		}
	}

}
