package com.cinemahallbooking.movie.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "locations")
public class LocationModel {

	@Id
	private String id;

	@Field("location_name")
	private String locationName;

	public String getId() {
		return id;
	}

}
