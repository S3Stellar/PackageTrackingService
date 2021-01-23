package com.example.demo.service;

import java.util.List;

import com.example.demo.boundary.TrackBoundary;

public interface PackageTrackingService {

	public TrackBoundary create(TrackBoundary track);

	public TrackBoundary getSpecificTrack(String trackId);

	public void updateTrack(TrackBoundary track, String trackId);

	public void deleteAll();

	public List<TrackBoundary> getTracksByApproximatedArrivalDate(String email, String value, String sortBy, String sortOrder,
			int page, int size);

	public List<TrackBoundary> getTracksByCreatedTimestamp(String email, String value, String sortBy, String sortOrder, int page,
			int size);

	public List<TrackBoundary> getTracksByStatus(String email, String value, String sortBy, String sortOrder, int page, int size);

	public List<TrackBoundary> getTracksByEmail(String email, String value, String sortBy, String sortOrder, int page, int size);

	public List<TrackBoundary> getTracksByShoppingCartId(String sortBy, String sortOrder, int page, int size);

}
