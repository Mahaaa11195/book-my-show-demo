package com.cinemahallbooking.movie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemahallbooking.movie.model.MovieModel;
import com.cinemahallbooking.movie.service.MovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {
	@Autowired
	private MovieService movieService;

	// save movie
	@PostMapping("/save")
	public ResponseEntity<?> addMovie(@RequestBody MovieModel movie) {
		return movieService.save(movie);
	}

	// get all movies
	@GetMapping("/get/all")
	public List<MovieModel> getAllMovies() {
		return movieService.getAllMovies();
	}
}
