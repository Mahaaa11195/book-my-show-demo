package com.cinemahallbooking.movie.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cinemahallbooking.movie.model.MovieModel;
import com.cinemahallbooking.movie.service.MovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {
	@Autowired
	private MovieService movieService;

	// save movie
	@PostMapping("/save")
	public ResponseEntity<?> addMovie(@RequestParam("movieTitle") String movieTitle,
			@RequestParam("genre") String genre,
			@RequestParam("releaseDate") String releaseDate, @RequestParam("image") MultipartFile imageFile) {

		try {
			MovieModel movie = new MovieModel();
			movie.setMovieTitle(movieTitle);
			movie.setGenre(genre);
			movie.setReleaseDate(releaseDate);
			// Convert the uploaded image file to a byte array and set it
			movie.setImage(imageFile.getBytes());

			return movieService.save(movie);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error uploading movie: " + e.getMessage());
		}
	}

	// get all movies
	@GetMapping("/get/all")
	public List<MovieModel> getAllMovies() {
		return movieService.getAllMovies();
	}
}
