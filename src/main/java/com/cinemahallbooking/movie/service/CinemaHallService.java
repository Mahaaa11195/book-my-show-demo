package com.cinemahallbooking.movie.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cinemahallbooking.movie.model.CinemaHallModel;

public interface CinemaHallService {
	ResponseEntity<?> save(CinemaHallModel cinemaHall);
	List<CinemaHallModel> getAllCinemaHall();
}
