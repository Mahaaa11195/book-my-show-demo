package com.cinemahallbooking.movie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemahallbooking.movie.model.CinemaHallModel;
import com.cinemahallbooking.movie.service.CinemaHallService;

@RestController
@RequestMapping("/cinemahall")
public class CinemaHallController {
	@Autowired
	private CinemaHallService cinemaHallService;

	// create cinema hall
	@PostMapping("/save")
	public ResponseEntity<?> addCinemaHall(@RequestBody CinemaHallModel cinemaHall) {
		return cinemaHallService.save(cinemaHall);
	}

	// get cinema hall list
	@GetMapping("/get/all")
	public List<CinemaHallModel> getAllCinemaHall() {
		return cinemaHallService.getAllCinemaHall();
	}
}
