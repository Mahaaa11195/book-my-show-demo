package com.cinemahallbooking.movie.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cinemahallbooking.movie.model.MovieModel;

public interface MovieService {
	ResponseEntity<?> save(MovieModel movie);

	List<MovieModel> getAllMovies();
}
