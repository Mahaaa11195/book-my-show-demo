package com.cinemahallbooking.movie.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinemahallbooking.movie.model.BookingModel;
import com.cinemahallbooking.movie.model.MovieModel;
import com.cinemahallbooking.movie.model.CinemaHallModel;
import com.cinemahallbooking.movie.model.SeatModel;
import com.cinemahallbooking.movie.repository.BookingRepository;
import com.cinemahallbooking.movie.repository.MovieRepository;
import com.cinemahallbooking.movie.repository.CinemaHallRepository;
import com.cinemahallbooking.movie.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Override
    public BookingModel createBooking(BookingModel bookingModel) {
        // Validate movie exists
        Optional<MovieModel> movie = movieRepository.findById(bookingModel.getMovie().getId());
        if (!movie.isPresent()) {
            throw new RuntimeException("Movie not found");
        }
        
        // Validate cinema hall exists
        Optional<CinemaHallModel> cinemaHall = cinemaHallRepository.findById(bookingModel.getCinemaHall().getId());
        if (!cinemaHall.isPresent()) {
            throw new RuntimeException("Cinema Hall not found");
        }

        // Validate seats exist in the cinema hall
        List<SeatModel> availableSeats = cinemaHall.get().getShowTimings().stream()
            .filter(showTiming -> showTiming.getTime().equals(bookingModel.getShowTime()))
            .findFirst()
            .map(showTiming -> showTiming.getAvailableSeats())
            .orElseThrow(() -> new RuntimeException("Show time not available"));

        for (SeatModel seat : bookingModel.getSelectedSeats()) {
            if (!availableSeats.contains(seat)) {
                throw new RuntimeException("Selected seat " + seat.getSeatNumber() + " is not available");
            }
        }

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