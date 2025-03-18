package com.cinemahallbooking.movie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemahallbooking.movie.model.BookingModel;
import com.cinemahallbooking.movie.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	// booking the movie
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createBooking(@RequestBody BookingModel bookingModel) {
		BookingModel createdBooking = bookingService.createBooking(bookingModel);

		Map<String, Object> response = new HashMap<>();
		response.put("status", "Booking successful");
		response.put("bookingDetails", createdBooking);

		return ResponseEntity.ok(response);
	}

	// get all bookings
	@GetMapping("/all")
	public ResponseEntity<List<BookingModel>> getAllBookings() {
		return ResponseEntity.ok(bookingService.getAllBookings());
	}

	// get particular booking
	@GetMapping("/{id}")
	public ResponseEntity<BookingModel> getBookingById(@PathVariable String id) {
		return ResponseEntity.ok(bookingService.getBookingById(id));
	}

	// cancel the booking
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelBooking(@PathVariable String id) {
		bookingService.cancelBooking(id);
		return ResponseEntity.noContent().build();
	}
}
