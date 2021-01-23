package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Converter.TrackConverter;
import com.example.demo.boundary.TrackBoundary;
import com.example.demo.consumers.RestUserConsumer;
import com.example.demo.dal.PackageTrackingDao;
import com.example.demo.data.Track;

@Service
public class PackageTrackingServiceWithDB implements PackageTrackingService {

	private PackageTrackingDao packageTrackingDao;
	private TrackConverter trackConverter;
	private RestUserConsumer restUserConsumer;
	
	@Autowired
	public void setRestUserConsumer(RestUserConsumer restUserConsumer) {
		this.restUserConsumer = restUserConsumer;
	}
	
	@Autowired
	public void setTrackConverter(TrackConverter trackConverter) {
		this.trackConverter = trackConverter;
	}

	@Autowired
	public void setPackageTrackingDao(PackageTrackingDao packageTrackingDao) {
		this.packageTrackingDao = packageTrackingDao;
	}

	@Override
	public TrackBoundary create(TrackBoundary trackBoundary) {
		
		restUserConsumer.getUser(trackBoundary.getUser().getEmail());
		Track track = this.trackConverter.toEntity(trackBoundary); 
		track = this.packageTrackingDao.save(track);
		return this.trackConverter.toBoundary(track);
	}
	
	@Override
	public TrackBoundary getSpecificTrack(String trackId) {
		
		return null;
	}

	@Override
	public void updateTrack(TrackBoundary track, String trackId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TrackBoundary> getTracksByApproximatedArrivalDate(String email, String value, String sortBy,
			String sortOrder, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackBoundary> getTracksByCreatedTimestamp(String email, String value, String sortBy, String sortOrder,
			int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackBoundary> getTracksByStatus(String email, String value, String sortBy, String sortOrder, int page,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackBoundary> getTracksByEmail(String email, String value, String sortBy, String sortOrder, int page,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackBoundary> getTracksByShoppingCartId(String sortBy, String sortOrder, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
