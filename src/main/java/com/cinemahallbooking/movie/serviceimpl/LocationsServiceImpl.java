package com.cinemahallbooking.movie.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cinemahallbooking.movie.model.LocationModel;
import com.cinemahallbooking.movie.repository.LocationsRepository;
import com.cinemahallbooking.movie.service.LocationsService;

@Service
public class LocationsServiceImpl implements LocationsService {
	@Autowired
	private LocationsRepository locationRepository;

	@Override
	public ResponseEntity<?> save(LocationModel location) {
		Optional<LocationModel> existingLocation = locationRepository.findByLocationName(location.getLocationName());

		if (existingLocation.isPresent()) {
			return new ResponseEntity<>("location with title '" + location.getLocationName() + "' already exists!",
					HttpStatus.CONFLICT);
		}
		locationRepository.save(location);
		return new ResponseEntity<>("location saved successfully!", HttpStatus.CREATED);
	}

	@Override
	public List<LocationModel> getAllLocations() {
		return locationRepository.findAll();
	}

}
