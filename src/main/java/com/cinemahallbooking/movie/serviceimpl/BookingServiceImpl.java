package com.cinemahallbooking.movie.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinemahallbooking.movie.model.BookingModel;
import com.cinemahallbooking.movie.model.CinemaHallModel;
import com.cinemahallbooking.movie.model.CreateMovieModel;
import com.cinemahallbooking.movie.model.LocationModel;
import com.cinemahallbooking.movie.model.MovieSchedule;
import com.cinemahallbooking.movie.model.SeatModel;
import com.cinemahallbooking.movie.model.ShowTimingModel;
import com.cinemahallbooking.movie.repository.BookingRepository;
import com.cinemahallbooking.movie.repository.CreateMovieRepository;
import com.cinemahallbooking.movie.service.BookingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private CreateMovieRepository createMovieRepository;

	@Override
	public BookingModel createBooking(BookingModel bookingModel) {
		LocalDateTime bookingTime = LocalDateTime.now();
		bookingModel.setBookingTime(bookingTime);

		// Validate movie exists
		Optional<CreateMovieModel> movieOpt = createMovieRepository.findByMovieTitle(bookingModel.getMovieTitle());
		if (!movieOpt.isPresent()) {
			throw new RuntimeException("Movie not found");
		}
		CreateMovieModel movie = movieOpt.get();
		log.info("movie" + movie);

		// Fetch MovieSchedule for the selected date
		@SuppressWarnings("unlikely-arg-type")
		LocalDate showDate = bookingModel.getShowDate();
		Optional<MovieSchedule> scheduleOpt = movie.getMovieSchedule().stream()
				.peek(schedule -> System.out.println("Schedule Date: " + schedule.getDate()))
				.filter(schedule -> schedule.getDate().isEqual(showDate)).findFirst();

		if (!scheduleOpt.isPresent()) {
			throw new RuntimeException("No schedule found for the selected date");
		}

		MovieSchedule movieSchedule = scheduleOpt.get();

		// Validate location exists in the schedule
		Optional<LocationModel> locationOpt = movieSchedule.getLocations().stream()
				.filter(loc -> loc.getId().equals(bookingModel.getLocationId())).findFirst();

		if (!locationOpt.isPresent()) {
			throw new RuntimeException("Location not available for the selected date");
		}

		// Validate cinema hall exists in the schedule
		Optional<CinemaHallModel> cinemaHallOpt = movieSchedule.getCinemaHalls().stream()
				.filter(hall -> hall.getId().equals(bookingModel.getCinemaHallId())).findFirst();

		if (!cinemaHallOpt.isPresent()) {
			throw new RuntimeException("Cinema Hall not available for the selected date");
		}
		CinemaHallModel cinemaHall = cinemaHallOpt.get();

		// Find the matching show time
		Optional<ShowTimingModel> showTimingOpt = cinemaHall.getShowTimings().stream()
				.filter(showTiming -> showTiming.getTime().equals(bookingModel.getShowTime())).findFirst();

		if (!showTimingOpt.isPresent()) {
			throw new RuntimeException("Show time not available");
		}

		ShowTimingModel showTiming = showTimingOpt.get();

		// Validate seats exist & update them as booked
		List<SeatModel> availableSeats = showTiming.getAvailableSeats();
		int totalPrice = 0;

		for (SeatModel seat : bookingModel.getSelectedSeats()) {
			Optional<SeatModel> seatOpt = availableSeats.stream()
					.filter(s -> s.getSeatNumber().equals(seat.getSeatNumber())).findFirst();

			if (!seatOpt.isPresent()) {
				throw new RuntimeException("Selected seat " + seat.getSeatNumber() + " is not available");
			}

			SeatModel seatToBook = seatOpt.get();

			if (seatToBook.isBooked()) {
				throw new RuntimeException("Seat " + seat.getSeatNumber() + " is already booked");
			}

			seatToBook.setBooked(true);
			totalPrice += seatToBook.getSeatPrice(); // Calculate total price
		}
		createMovieRepository.save(movie);

		// Update total price in booking
		bookingModel.setTotalPrice(totalPrice);

		// Save the booking
		return bookingRepository.save(bookingModel);
	}

	@Override
	public List<BookingModel> getAllBookings() {
		return bookingRepository.findAll();
	}

	@Override
	public BookingModel getBookingById(String id) {
		return bookingRepository.findById(id).orElse(null);
	}

	@Override
	public void cancelBooking(String id) {
		bookingRepository.deleteById(id);
	}
}