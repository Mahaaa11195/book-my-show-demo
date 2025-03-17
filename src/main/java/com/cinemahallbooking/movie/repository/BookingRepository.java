package com.cinemahallbooking.movie.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.cinemahallbooking.movie.model.BookingModel;

public interface BookingRepository extends MongoRepository<BookingModel, String> {
}