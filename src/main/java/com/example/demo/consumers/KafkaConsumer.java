package com.example.demo.consumers;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.boundary.TrackBoundary;
import com.example.demo.service.PackageTrackingService;

@Configuration
public class KafkaConsumer {

	private PackageTrackingService packageTrackingService;

	@Autowired
	public void setPackageTrackingService(PackageTrackingService packageTrackingService) {
		this.packageTrackingService = packageTrackingService;
	}

	@Bean
	public Consumer<TrackBoundary> receiveAndHandlePostTrack() {
		return t->packageTrackingService.create(t);
	}
}
