package com.cinemahallbooking.movie.model;


import java.util.List;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimingModel {
    
    @Field("time")
    private String time;  // Example: "9 AM"

    @Field("available_seats")
    private List<SeatModel> availableSeats;
}

