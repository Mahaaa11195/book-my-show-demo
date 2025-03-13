package com.cinemahallbooking.movie.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // ✅ Add this annotation to generate a no-arg constructor
public class SeatModel {

    @Field("seat_number")
    private String seatNumber;

    @Field("seat_price")
    private int seatPrice;

    @Field("is_booked")
    private boolean isBooked = false;

    public SeatModel(String seatNumber, int seatPrice, boolean isBooked) {
        this.seatNumber = seatNumber;
        this.seatPrice = seatPrice;
        this.isBooked = isBooked;
    }
}
