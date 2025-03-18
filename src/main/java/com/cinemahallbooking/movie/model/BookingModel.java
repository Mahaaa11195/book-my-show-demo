package com.cinemahallbooking.movie.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bookings")
public class BookingModel {

	@Id
	private String id;

//    @Field("user_id")
//    private String userId; // User who booked the movie

	@Field("location_id")
	private String locationId;

	@Field("movie_title")
	private String movieTitle;

	@Field("cinema_hall_id")
	private String cinemaHallId;

	@Field("show_date")
	private LocalDate showDate;

	@Field("show_time")
	private String showTime;

	@Field("selected_seats")
	private List<SeatModel> selectedSeats;

	// no need to pass in the payload bcz added in code level

	@Field("booking_time")
	private LocalDateTime bookingTime;

	@Field("total_price")
	private int totalPrice;
}