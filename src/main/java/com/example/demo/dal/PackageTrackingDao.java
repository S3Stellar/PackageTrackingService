package com.example.demo.dal;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.data.Track;

public interface PackageTrackingDao extends PagingAndSortingRepository<Track, String>{

}
