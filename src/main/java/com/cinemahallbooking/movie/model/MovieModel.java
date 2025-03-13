package com.cinemahallbooking.movie.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "movie")
public class MovieModel {

	@Id
	private String id;

	@Field("movie_title")
	private String movieTitle; 

	@Field("genre")
	private String genre;

	@Field("date")
	private LocalDateTime date;

	@Field("image")
	private String image;

	@Field("release_date")
	private String releaseDate;

}
