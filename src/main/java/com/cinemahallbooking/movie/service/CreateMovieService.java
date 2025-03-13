package com.cinemahallbooking.movie.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.cinemahallbooking.movie.model.CreateMovieModel;

public interface CreateMovieService {
	ResponseEntity<?> save(CreateMovieModel createMovie);

	List<CreateMovieModel> getAllCreatedMovies();
	
	ResponseEntity<?> updateMovie(String movieId, CreateMovieModel updatedRequest);
	ResponseEntity<?> updateMovie1(String movieId, Map<String, Object> updates);
}
