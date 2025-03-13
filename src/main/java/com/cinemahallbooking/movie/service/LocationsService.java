package com.cinemahallbooking.movie.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cinemahallbooking.movie.model.LocationModel;

public interface LocationsService {
	ResponseEntity<?> save(LocationModel location);

	List<LocationModel> getAllLocations();
}
