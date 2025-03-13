package com.cinemahallbooking.movie.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cinemahallbooking.movie.model.MovieModel;
import com.cinemahallbooking.movie.repository.MovieRepository;
import com.cinemahallbooking.movie.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Override
	public ResponseEntity<?> save(MovieModel movie) {
		Optional<MovieModel> existingMovie = movieRepository.findByMovieTitle(movie.getMovieTitle());

		if (existingMovie.isPresent()) {
			return new ResponseEntity<>("Movie with title '" + movie.getMovieTitle() + "' already exists!",
					HttpStatus.CONFLICT);
		}
		LocalDateTime movieAddedTime = LocalDateTime.now();
		movie.setDate(movieAddedTime);
		movieRepository.save(movie);
		return new ResponseEntity<>("Movie saved successfully!", HttpStatus.CREATED);
	}

	@Override
	public List<MovieModel> getAllMovies() {
		return movieRepository.findAll();
	}

}
