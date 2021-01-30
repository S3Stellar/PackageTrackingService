package com.example.demo.Converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.example.demo.boundary.TrackBoundary;
import com.example.demo.data.Track;
import com.example.demo.exceptions.ApproximatedDateParseException;

@Component
public class TrackConverter {
	private SimpleDateFormat dateFormat;

	@PostConstruct
	public void setDateFormat() {
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	public Track toEntity(TrackBoundary trackBoundary) {

		try {
			return new Track(trackBoundary.getUser(), trackBoundary.getOrder(), trackBoundary.getAddress(), new Date(),
					dateFormat.parse(trackBoundary.getApproximatedArrivalDate()), trackBoundary.getStatus(),
					trackBoundary.getDescription(), new ArrayList<>());
		} catch (Exception e) {
			throw new ApproximatedDateParseException("Incorret approximated date format");
		}

	}

	public TrackBoundary toBoundary(Track track) {
		return new TrackBoundary(track.getTrackId(), track.getUser(), track.getOrder(), track.getAddress(),
				track.getCreatedTimestamp(), dateFormat.format(track.getApproximatedArrivalDate()), track.getStatus(),
				track.getDescription(), track.getHistory());
	}
}
