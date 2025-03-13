package com.cinemahallbooking.movie.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cinemahallbooking.movie.model.CreateMovieModel;
import com.cinemahallbooking.movie.service.CreateMovieService;

@RestController
@RequestMapping("/movie")  // Changed to plural for RESTful convention
public class CreateMovieController {

    @Autowired
    private CreateMovieService createMovieService;

    // Create a new movie
    @PostMapping("/create")
    public ResponseEntity<?> addMovie(@RequestBody CreateMovieModel movie) {
        return createMovieService.save(movie);
    }

    // Get all movies
    @GetMapping("/all")
    public List<CreateMovieModel> getAllMovies() {
        return createMovieService.getAllCreatedMovies();
    }

//    // Get a specific movie by ID
//    @GetMapping("/{movieId}")
//    public ResponseEntity<?> getMovieById(@PathVariable String movieId) {
//        return createMovieService.getMovieById(movieId);
//    }

    // Update an existing movie
    @PutMapping("/update/{movieId}")
    public ResponseEntity<?> updateMovie(@PathVariable String movieId, @RequestBody CreateMovieModel updatedMovie) {
        return createMovieService.updateMovie(movieId, updatedMovie);
    }
    @PatchMapping("/{movieId}")
    public ResponseEntity<?> updateMovie1(@PathVariable String movieId, @RequestBody Map<String, Object> updates) {
        return createMovieService.updateMovie1(movieId, updates);
    }

//    // Delete a movie by ID
//    @DeleteMapping("/delete/{movieId}")
//    public ResponseEntity<?> deleteMovie(@PathVariable String movieId) {
//        return createMovieService.deleteMovie(movieId);
//    }
}
