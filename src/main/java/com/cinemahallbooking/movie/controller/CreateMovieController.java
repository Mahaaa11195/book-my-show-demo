package com.cinemahallbooking.movie.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cinemahallbooking.movie.model.CreateMovieModel;
import com.cinemahallbooking.movie.service.CreateMovieService;

@RestController
@RequestMapping("/movie")
public class CreateMovieController {

	@Autowired
	private CreateMovieService createMovieService;

	// create a movie
//	@PostMapping("/create")
//	public ResponseEntity<?> addMovie(@RequestBody CreateMovieModel movie) {
//		return createMovieService.save(movie);
//	}

	@PostMapping("/create")
	public ResponseEntity<?> addMovie(@RequestPart("movie") CreateMovieModel movie,
			@RequestParam("image") MultipartFile imageFile) {

		try {
			// Set the image from MultipartFile
			movie.setImage(imageFile.getBytes());

			// Save the movie to the database
			return createMovieService.save(movie);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error uploading movie: " + e.getMessage());
		}
	}

//	@PostMapping(value = "/create", consumes = { "multipart/form-data" })
//	public ResponseEntity<?> addMovie(@RequestPart("movie") String movieJson, // JSON as String
//			@RequestPart("image") MultipartFile image) { // Image file
//
//		try {
//			// Convert JSON String to Java Object
//			ObjectMapper objectMapper = new ObjectMapper();
//			MovieModel movie = objectMapper.readValue(movieJson, MovieModel.class);
//
//			return movieService.save(movie);
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
//		}
//	}

	// get all movie list
	@GetMapping("/all")
	public List<CreateMovieModel> getAllMovies() {
		return createMovieService.getAllCreatedMovies();
	}

	// update whole movie
	@PutMapping("/update/{movieId}")
	public ResponseEntity<?> updateMovie(@PathVariable String movieId, @RequestBody CreateMovieModel updatedMovie) {
		return createMovieService.updateMovie(movieId, updatedMovie);
	}

	// update particular field in movie object
	@PatchMapping("/{movieId}")
	public ResponseEntity<?> updateMovie1(@PathVariable String movieId, @RequestBody Map<String, Object> updates) {
		return createMovieService.updateMovie1(movieId, updates);
	}

	// delete movie
	@DeleteMapping("/delete/{movieId}")
	public ResponseEntity<?> deleteMovie(@PathVariable String movieId) {
		return createMovieService.deleteMovie(movieId);
	}
}
