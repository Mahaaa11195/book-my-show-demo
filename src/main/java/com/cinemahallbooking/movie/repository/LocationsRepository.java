package com.cinemahallbooking.movie.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cinemahallbooking.movie.model.LocationModel;

@Repository
public interface LocationsRepository extends MongoRepository<LocationModel, String> {

	Optional<LocationModel> findByLocationName(String locationName);

}
