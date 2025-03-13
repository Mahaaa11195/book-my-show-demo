package com.cinemahallbooking.movie.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cinemahallbooking.movie.model.MovieModel;
@Repository
public interface MovieRepository extends MongoRepository<MovieModel, String> {

	Optional<MovieModel> findByMovieTitle(String movieTitle);

}
