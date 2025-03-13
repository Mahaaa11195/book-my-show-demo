package com.cinemahallbooking.movie.model;

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
    
    @Field("user_id")
    private String userId; // User who booked the movie
    
    @Field("movie")
    private MovieModel movie; // Movie details
    
    @Field("cinema_hall")
    private CinemaHallModel cinemaHall; // Selected cinema hall
    
    @Field("show_time")
    private String showTime; // Selected show timing
    
    @Field("selected_seats")
    private List<SeatModel> selectedSeats; // List of booked seats
    
    @Field("total_price")
    private int totalPrice; // Total booking price
    
    @Field("booking_time")
    private LocalDateTime bookingTime; // Timestamp of booking
    
    @Field("status")
    private String status; // Status of booking (e.g., "Confirmed", "Cancelled")
}