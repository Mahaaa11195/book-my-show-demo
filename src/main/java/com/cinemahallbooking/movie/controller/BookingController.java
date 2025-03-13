package com.cinemahallbooking.movie.controller;

import java.util.List;

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

	@PostMapping("/create")
	public ResponseEntity<BookingModel> createBooking(@RequestBody BookingModel bookingModel) {
		return ResponseEntity.ok(bookingService.createBooking(bookingModel));
	}

	@GetMapping("/all")
	public ResponseEntity<List<BookingModel>> getAllBookings() {
		return ResponseEntity.ok(bookingService.getAllBookings());
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookingModel> getBookingById(@PathVariable String id) {
		return ResponseEntity.ok(bookingService.getBookingById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelBooking(@PathVariable String id) {
		bookingService.cancelBooking(id);
		return ResponseEntity.noContent().build();
	}
}
