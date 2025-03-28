package com.cinemahallbooking.movie.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cinemahallbooking.movie.model.CreateMovieModel;

public interface CreateMovieRepository extends MongoRepository<CreateMovieModel, String> {

	Optional<CreateMovieModel> findByMovieTitle(String movieTitle);

	boolean existsByReleaseDateAndLocations_IdAndCinemaHalls_IdAndCinemaHalls_ShowTimings_Time(LocalDate releaseDate,
			List<String> locationIds, String cinemaHallId, String showTime);

}
