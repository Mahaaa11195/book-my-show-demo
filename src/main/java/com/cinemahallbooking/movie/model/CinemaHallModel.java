package com.cinemahallbooking.movie.model;
import java.util.ArrayList;
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
@Document(collection = "cinemaHall")
public class CinemaHallModel {
    @Id
    private String id;

    @Field("cinema_hall_name") 
    private String cinemaHallName;

    @Field("show_timings")
    private List<ShowTimingModel> showTimings = new ArrayList<>();

    @Field("total_seats")
    private int totalSeats = 5; 
    
    @Field("cinema_hall_location_name")
    private String cinemaHallLocationName;
}
