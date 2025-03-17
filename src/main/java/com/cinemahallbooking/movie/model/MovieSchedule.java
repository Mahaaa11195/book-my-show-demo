package com.cinemahallbooking.movie.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSchedule {

	@Field("date")
	private LocalDate date;

	@Field("locations")
	private List<LocationModel> locations;

	@Field("cinemaHalls")
	private List<CinemaHallModel> cinemaHalls;
}
