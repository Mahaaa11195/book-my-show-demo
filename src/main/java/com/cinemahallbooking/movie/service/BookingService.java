package com.cinemahallbooking.movie.service;

import java.util.List;

import com.cinemahallbooking.movie.model.BookingModel;

public interface BookingService {
	BookingModel createBooking(BookingModel bookingModel);

	List<BookingModel> getAllBookings();

	BookingModel getBookingById(String id);

	void cancelBooking(String id);
}