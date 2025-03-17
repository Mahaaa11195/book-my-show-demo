package com.cinemahallbooking.movie.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	@Field("movie_schedule")
	private List<MovieSchedule> movieSchedule = new ArrayList<>();

	@Field("locations") // Store full location objects
	private List<LocationModel> locations = new ArrayList<>();

	@Field("cinemaHalls") // Store full cinema hall objects
	private List<CinemaHallModel> cinemaHalls = new ArrayList<>();

	// Constructor to auto-calculate release end date (6 days after release)
//	public CreateMovieModel(CreateMovieModel request, List<LocationModel> locations,
//			List<CinemaHallModel> cinemaHalls) {
//		this.movieTitle = request.getMovieTitle();
//		this.genre = request.getGenre();
//		this.image = request.getImage();
//
//		if (request.getReleaseDate() != null) {
//			this.releaseDate = request.getReleaseDate();
//			this.releaseEndDate = this.releaseDate.plusDays(6);
//		} else {
//			this.releaseDate = null;
//			this.releaseEndDate = null;
//		}
//
//		this.locations = locations != null ? locations : new ArrayList<>();
//		this.cinemaHalls = cinemaHalls != null ? cinemaHalls : new ArrayList<>();
//		this.movieSchedule = generateMovieSchedule();
//
//	}
//
//	// Method to generate schedule from releaseDate to releaseEndDate
//	private List<MovieSchedule> generateMovieSchedule() {
//		if (releaseDate == null || releaseEndDate == null) {
//			return new ArrayList<>();
//		}
//
//		return IntStream.rangeClosed(0, (int) releaseDate.until(releaseEndDate).getDays())
//				.mapToObj(i -> new MovieSchedule(releaseDate.plusDays(i), locations, cinemaHalls))
//				.collect(Collectors.toList());
//	}
	public CreateMovieModel(CreateMovieModel request, List<LocationModel> locations, List<CinemaHallModel> allCinemaHalls) {
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

	    this.locations = locations != null ? new ArrayList<>(locations) : new ArrayList<>();

	    // Debugging - Print all fetched cinema halls
	    System.out.println("All Cinema Halls: ");
	    allCinemaHalls.forEach(cinema -> System.out.println(cinema.getCinemaHallLocationName()));

	    System.out.println("Locations:");
	    locations.forEach(loc -> System.out.println(loc.getLocationName()));

	    // Filter cinema halls based on location names
	    List<CinemaHallModel> filteredCinemaHalls = allCinemaHalls.stream()
	            .filter(cinemaHall -> locations.stream()
	                    .map(LocationModel::getLocationName) // Compare with location name
	                    .anyMatch(name -> name.equals(cinemaHall.getCinemaHallLocationName())))
	            .collect(Collectors.toList());

	    System.out.println("Filtered Cinema Halls Count: " + filteredCinemaHalls.size());

	    // Assign filtered cinema halls
	    this.cinemaHalls = new ArrayList<>(filteredCinemaHalls);

	    // Generate movie schedule after setting locations and cinema halls
	    this.movieSchedule = generateMovieSchedule();
	}
	// Method to generate schedule from releaseDate to releaseEndDate
	private List<MovieSchedule> generateMovieSchedule() {
		if (releaseDate == null || releaseEndDate == null) {
			return new ArrayList<>();
		}

		return IntStream.rangeClosed(0, (int) releaseDate.until(releaseEndDate).getDays())
				.mapToObj(i -> new MovieSchedule(releaseDate.plusDays(i), locations, cinemaHalls))
				.collect(Collectors.toList());
	}

}
