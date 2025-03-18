package com.cinemahallbooking.movie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemahallbooking.movie.model.LocationModel;
import com.cinemahallbooking.movie.service.LocationsService;

@RestController
@RequestMapping("/locations")
public class LocationsController {
	@Autowired
	private LocationsService locationService;

	// create location
	@PostMapping("/save")
	public ResponseEntity<?> addLocation(@RequestBody LocationModel location) {
		return locationService.save(location);
	}

	// get all location list
	@GetMapping("/get/all")
	public List<LocationModel> getAllLocations() {
		return locationService.getAllLocations();
	}
}
