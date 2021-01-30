package com.example.demo.dal;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.data.Track;

public interface PackageTrackingDao extends PagingAndSortingRepository<Track, String> {

	public List<Track> findAllByUser_emailAndApproximatedArrivalDateGreaterThanEqual(@Param("email") String email,
			@Param("date") Date date, Pageable pageable);
	
	
	public List<Track> findAllByUser_emailAndCreatedTimestampGreaterThanEqual(@Param("email") String email,
			@Param("date") Date date, Pageable pageable);
	
	
	public List<Track> findAllByUser_email(@Param("email") String email, Pageable pageable);
	
	
}

