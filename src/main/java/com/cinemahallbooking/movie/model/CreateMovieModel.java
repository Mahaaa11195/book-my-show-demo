package com.cinemahallbooking.movie.model;

import java.time.LocalDate;
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
@Document(collection = "createMovie")
public class CreateMovieModel {
    
    @Id
    private String id;
   

        
    @Field("movie_title")
    private String movieTitle; 

    @Field("genre")
    private String genre;

    @Field("image")
    private String image;

    @Field("release_date")
    private LocalDate releaseDate;

    @Field("release_end_date")
    private LocalDate releaseEndDate;

    @Field("locations")  // Store full location objects
    private List<LocationModel> locations = new ArrayList<>();

    @Field("cinemaHalls")  // Store full cinema hall objects
    private List<CinemaHallModel> cinemaHalls = new ArrayList<>();



//	private Object locationIds;
//
//
//
//	private Object cinemaHallIds;


    // Constructor to auto-calculate release end date (6 days after release)
    public CreateMovieModel(CreateMovieModel request, List<LocationModel> locations, List<CinemaHallModel> cinemaHalls) {
        this.movieTitle = request.getMovieTitle();
        this.genre = request.getGenre();
        this.image = request.getImage();
        
        if (request.getReleaseDate() != null) {
            this.releaseDate = request.getReleaseDate();
            this.releaseEndDate = this.releaseDate.plusDays(6);
        } else {
            this.releaseDate = null;
            this.releaseEndDate = null;
        }

        this.locations = locations != null ? locations : new ArrayList<>();
        this.cinemaHalls = cinemaHalls != null ? cinemaHalls : new ArrayList<>();
    }



}

