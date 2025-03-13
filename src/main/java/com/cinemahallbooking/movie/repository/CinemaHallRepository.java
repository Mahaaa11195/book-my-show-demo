package com.cinemahallbooking.movie.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cinemahallbooking.movie.model.CinemaHallModel;

@Repository
public interface CinemaHallRepository extends MongoRepository<CinemaHallModel, String> {

	Optional<CinemaHallModel> findByCinemaHallName(String cinemaHallName);

}
