package com.cinemahallbooking.movie.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cinemahallbooking.movie.model.CinemaHallModel;
import com.cinemahallbooking.movie.model.SeatModel;
import com.cinemahallbooking.movie.model.ShowTimingModel;
import com.cinemahallbooking.movie.repository.CinemaHallRepository;
import com.cinemahallbooking.movie.service.CinemaHallService;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {
	@Autowired
	private CinemaHallRepository cinemaHallRepository;

	@Override
	public ResponseEntity<?> save(CinemaHallModel cinemaHall) {
		Optional<CinemaHallModel> existingCinemaHall = cinemaHallRepository
				.findByCinemaHallName(cinemaHall.getCinemaHallName());

		if (existingCinemaHall.isPresent()) {
			return new ResponseEntity<>("CinemaHall with name '" + cinemaHall.getCinemaHallName() + "' already exists!",
					HttpStatus.CONFLICT);
		}

		// Generate seats for each showTiming
		for (ShowTimingModel show : cinemaHall.getShowTimings()) {
			show.setAvailableSeats(List.of(new SeatModel("A1", 200, false), new SeatModel("A2", 200, false),
					new SeatModel("A3", 150, false), new SeatModel("A4", 150, false), new SeatModel("A5", 150, false)));
		}

		cinemaHallRepository.save(cinemaHall);
		return new ResponseEntity<>("CinemaHall saved successfully!", HttpStatus.CREATED);
	}

	@Override
	public List<CinemaHallModel> getAllCinemaHall() {
		return cinemaHallRepository.findAll();
	}

}
