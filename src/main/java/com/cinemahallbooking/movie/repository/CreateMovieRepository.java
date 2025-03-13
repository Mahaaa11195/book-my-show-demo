package com.cinemahallbooking.movie.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cinemahallbooking.movie.model.CreateMovieModel;
import com.cinemahallbooking.movie.model.LocationModel;

public interface CreateMovieRepository extends MongoRepository<CreateMovieModel, String> {

	Optional<CreateMovieModel> findByMovieTitle(String movieTitle);

}
