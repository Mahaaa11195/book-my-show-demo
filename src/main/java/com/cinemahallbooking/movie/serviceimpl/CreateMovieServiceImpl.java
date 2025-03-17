package com.cinemahallbooking.movie.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cinemahallbooking.movie.model.CinemaHallModel;
import com.cinemahallbooking.movie.model.CreateMovieModel;
import com.cinemahallbooking.movie.model.LocationModel;
import com.cinemahallbooking.movie.model.SeatModel;
import com.cinemahallbooking.movie.model.ShowTimingModel;
import com.cinemahallbooking.movie.repository.CinemaHallRepository;
import com.cinemahallbooking.movie.repository.CreateMovieRepository;
import com.cinemahallbooking.movie.repository.LocationsRepository;
import com.cinemahallbooking.movie.service.CreateMovieService;

@Service
public class CreateMovieServiceImpl implements CreateMovieService {

	@Autowired
	private CreateMovieRepository createMovieRepository;

	@Autowired
	private CinemaHallRepository cinemaHallRepository;

	@Autowired
	private LocationsRepository locationRepository; // To fetch locations

	/**
	 * Saves a new movie while ensuring referenced locations & cinema halls exist.
	 */
	public ResponseEntity<?> save(CreateMovieModel request) {
		// Check if a movie with the same title already exists
		Optional<CreateMovieModel> existingMovie = createMovieRepository.findByMovieTitle(request.getMovieTitle());
		if (existingMovie.isPresent()) {
			return new ResponseEntity<>("Movie with title '" + request.getMovieTitle() + "' already exists!",
					HttpStatus.CONFLICT);
		}

		// Fetch locations based on the provided location IDs
		List<String> locationIds = request.getLocations().stream().map(LocationModel::getId)
				.collect(Collectors.toList());
		List<LocationModel> locations = locationRepository.findAllById(locationIds);

		// Debugging - Check if locationIds are correct
		System.out.println("Location IDs from request: " + locationIds);
		System.out.println("Locations fetched: " + locations.stream().map(LocationModel::getLocationName).toList());

		// Fetch all cinema halls
		List<CinemaHallModel> allCinemaHalls = cinemaHallRepository.findAll();

		// Debugging - Check all cinema halls fetched
		System.out.println("All Cinema Halls: ");
		allCinemaHalls.forEach(cinema -> System.out.println(cinema.getCinemaHallLocationName()));
		System.out.println("Locations:");
		locations.forEach(loc -> System.out.println(loc.getLocationName()));

		System.out.println("All Cinema Halls:");
		allCinemaHalls.forEach(ch -> System.out.println(ch.getCinemaHallLocationName()));

		System.out.println("Filtered Cinema Halls:");

		List<CinemaHallModel> filteredCinemaHalls = allCinemaHalls.stream()
				.filter(cinemaHall -> locations.stream().map(LocationModel::getLocationName) // Compare with location
																								// name
						.anyMatch(name -> {
							boolean match = name.equals(cinemaHall.getCinemaHallLocationName());
							if (match) {
								System.out.println("Matched: " + cinemaHall.getCinemaHallLocationName());
							}
							return match;
						}))
				.collect(Collectors.toList());

		System.out.println("Filtered Cinema Halls Count: " + filteredCinemaHalls.size());

		// Generate seats for valid cinema halls
		for (CinemaHallModel cinemaHall : filteredCinemaHalls) {
			request.getCinemaHalls().stream().filter(reqCinema -> reqCinema.getId().equals(cinemaHall.getId()))
					.forEach(reqCinema -> {

						reqCinema.getShowTimings().forEach(showTiming -> {
							if (showTiming.getAvailableSeats() == null) { // Ensure seats are created for all showtimes
								List<SeatModel> seats = generateSeats(cinemaHall.getTotalSeats());
								showTiming.setAvailableSeats(seats);
							}
						});
						cinemaHall.setShowTimings(reqCinema.getShowTimings());
					});
		}

		// Save the new movie with the filtered cinema halls
		CreateMovieModel newMovie = new CreateMovieModel(request, locations, filteredCinemaHalls);
		createMovieRepository.save(newMovie);

		return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
	}

	/**
	 * Generates a list of seats based on total seats.
	 */
	private List<SeatModel> generateSeats(int totalSeats) {
		List<SeatModel> seats = new ArrayList<>();
		for (int i = 1; i <= totalSeats; i++) {
			seats.add(new SeatModel("A" + i, i <= 2 ? 200 : 150)); // First 2 seats: 200, others: 150
		}
		return seats;
	}

	/**
	 * Retrieves all created movies.
	 */
	public List<CreateMovieModel> getAllCreatedMovies() {
		List<CreateMovieModel> movies = createMovieRepository.findAll();

		// Fetch full cinema hall details for each movie
		movies.forEach(movie -> {
			List<String> cinemaHallIds = movie.getCinemaHalls().stream().map(CinemaHallModel::getId)
					.collect(Collectors.toList());
			List<CinemaHallModel> fullCinemaHalls = cinemaHallRepository.findAllById(cinemaHallIds);
			movie.setCinemaHalls(fullCinemaHalls); // Replace with full objects
		});

		return movies;
	}

//	public ResponseEntity<?> updateMovie(String movieId, CreateMovieModel updatedMovie) {
//	    Optional<CreateMovieModel> existingMovieOpt = createMovieRepository.findById(movieId);
//	    System.out.println("Checking movieId: " + movieId);
//	    Optional<CreateMovieModel> movieCheck = createMovieRepository.findById(movieId);
//	    System.out.println("Movie exists? " + movieCheck.isPresent());
//	    if (existingMovieOpt.isEmpty()) {
//	        return new ResponseEntity<>("Movie not found!", HttpStatus.NOT_FOUND);
//	    }
//
//	    CreateMovieModel existingMovie = existingMovieOpt.get();
//
//	    // Update basic details
//	    existingMovie.setMovieTitle(updatedMovie.getMovieTitle());
//	    existingMovie.setGenre(updatedMovie.getGenre());
//	    existingMovie.setImage(updatedMovie.getImage());
//	    existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
//
//	    // ✅ Auto-calculate releaseEndDate if not provided
//	    if (updatedMovie.getReleaseDate() != null) {
//	        existingMovie.setReleaseEndDate(updatedMovie.getReleaseDate().plusDays(6));
//	    }
//
//	    // Update locations
//	    List<String> locationIds = updatedMovie.getLocations().stream().map(LocationModel::getId).collect(Collectors.toList());
//	    List<LocationModel> locations = locationRepository.findAllById(locationIds);
//	    existingMovie.setLocations(locations);
//
//	    // Update cinema halls
//	    List<String> cinemaHallIds = updatedMovie.getCinemaHalls().stream().map(CinemaHallModel::getId).collect(Collectors.toList());
//	    List<CinemaHallModel> cinemaHalls = cinemaHallRepository.findAllById(cinemaHallIds);
//
//	    for (CinemaHallModel cinemaHall : cinemaHalls) {
//	        updatedMovie.getCinemaHalls().stream()
//	            .filter(reqCinema -> reqCinema.getId().equals(cinemaHall.getId()))
//	            .findFirst()
//	            .ifPresent(reqCinema -> {
//	                // ✅ Preserve `availableSeats` if missing in request
//	                reqCinema.getShowTimings().forEach(reqTiming -> {
//	                    reqTiming.setAvailableSeats(
//	                        cinemaHall.getShowTimings().stream()
//	                            .filter(existingTiming -> existingTiming.getTime().equals(reqTiming.getTime()))
//	                            .findFirst()
//	                            .map(existingTiming -> existingTiming.getAvailableSeats() != null ? existingTiming.getAvailableSeats() : reqTiming.getAvailableSeats())
//	                            .orElse(reqTiming.getAvailableSeats())  // Keep existing seats if available, else use the new request data
//  // If no previous seats, keep it null
//	                    );
//	                });
//
//	                // ✅ Update show timings
//	                cinemaHall.setShowTimings(reqCinema.getShowTimings());
//	            });
//	    }
//
//	    existingMovie.setCinemaHalls(cinemaHalls);
//
//	    // Save updated movie
//	    createMovieRepository.save(existingMovie);
//	    return new ResponseEntity<>(existingMovie, HttpStatus.OK);
//	}
	public ResponseEntity<?> updateMovie(String movieId, CreateMovieModel updatedMovie) {
		Optional<CreateMovieModel> existingMovieOpt = createMovieRepository.findById(movieId);
		if (existingMovieOpt.isEmpty()) {
			return new ResponseEntity<>("Movie not found!", HttpStatus.NOT_FOUND);
		}

		CreateMovieModel existingMovie = existingMovieOpt.get();

		// Update basic details
		existingMovie.setMovieTitle(updatedMovie.getMovieTitle());
		existingMovie.setGenre(updatedMovie.getGenre());
		existingMovie.setImage(updatedMovie.getImage());
		existingMovie.setReleaseDate(updatedMovie.getReleaseDate());

		if (updatedMovie.getReleaseDate() != null) {
			existingMovie.setReleaseEndDate(updatedMovie.getReleaseDate().plusDays(6));
		}

		// ✅ Filter locations
		List<String> locationIds = updatedMovie.getLocations().stream().map(LocationModel::getId)
				.collect(Collectors.toList());
		List<LocationModel> locations = locationRepository.findAllById(locationIds);
		existingMovie.setLocations(locations);

		// ✅ Fetch all cinema halls and filter based on location names
		List<CinemaHallModel> allCinemaHalls = cinemaHallRepository.findAll();
		List<CinemaHallModel> filteredCinemaHalls = allCinemaHalls.stream()
				.filter(cinemaHall -> locations.stream().map(LocationModel::getLocationName)
						.anyMatch(name -> name.equals(cinemaHall.getCinemaHallLocationName())))
				.collect(Collectors.toList());

		// ✅ Update cinema halls based on filtered halls
		for (CinemaHallModel cinemaHall : filteredCinemaHalls) {
			updatedMovie.getCinemaHalls().stream().filter(reqCinema -> reqCinema.getId().equals(cinemaHall.getId()))
					.findFirst().ifPresent(reqCinema -> {
						reqCinema.getShowTimings().forEach(reqTiming -> {
							reqTiming.setAvailableSeats(cinemaHall.getShowTimings().stream()
									.filter(existingTiming -> existingTiming.getTime().equals(reqTiming.getTime()))
									.findFirst()
									.map(existingTiming -> existingTiming.getAvailableSeats() != null
											? existingTiming.getAvailableSeats()
											: reqTiming.getAvailableSeats())
									.orElse(reqTiming.getAvailableSeats()));
						});

						cinemaHall.setShowTimings(reqCinema.getShowTimings());
					});
		}

		existingMovie.setCinemaHalls(filteredCinemaHalls);
		createMovieRepository.save(existingMovie);

		return new ResponseEntity<>(existingMovie, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
//	@PatchMapping("/movies/{movieId}")
	public ResponseEntity<?> updateMovie1(String movieId, Map<String, Object> updates) {
		Optional<CreateMovieModel> existingMovieOpt = createMovieRepository.findById(movieId);
		if (existingMovieOpt.isEmpty()) {
			return new ResponseEntity<>("Movie not found!", HttpStatus.NOT_FOUND);
		}

		CreateMovieModel existingMovie = existingMovieOpt.get();
		System.out.println("Received Updates: " + updates);

		// ✅ Use a mutable list (ArrayList) to allow modification
		List<LocationModel> updatedLocations = new ArrayList<>(existingMovie.getLocations());
		List<CinemaHallModel> updatedCinemaHalls = new ArrayList<>(existingMovie.getCinemaHalls());

		for (Map.Entry<String, Object> entry : updates.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			switch (key) {
			case "movieTitle":
				existingMovie.setMovieTitle((String) value);
				break;
			case "genre":
				existingMovie.setGenre((String) value);
				break;
			case "image":
				existingMovie.setImage((String) value);
				break;
			case "releaseDate":
				LocalDate newDate = LocalDate.parse((String) value);
				existingMovie.setReleaseDate(newDate);
				existingMovie.setReleaseEndDate(newDate.plusDays(6));
				break;
			case "locations":
				List<String> locationIds = ((List<Map<String, String>>) value).stream().map(loc -> loc.get("id"))
						.collect(Collectors.toList());
				updatedLocations.clear(); // ✅ Clear the existing list instead of reassigning
				updatedLocations.addAll(locationRepository.findAllById(locationIds));
				existingMovie.setLocations(updatedLocations);

				// ✅ Filter cinema halls dynamically based on updated locations
				List<CinemaHallModel> allCinemaHalls = cinemaHallRepository.findAll();
				updatedCinemaHalls.clear(); // ✅ Clear instead of reassigning
				updatedCinemaHalls.addAll(allCinemaHalls.stream()
						.filter(hall -> updatedLocations.stream().map(LocationModel::getLocationName)
								.anyMatch(locationName -> locationName.equals(hall.getCinemaHallLocationName())))
						.collect(Collectors.toList()));

				existingMovie.setCinemaHalls(updatedCinemaHalls);
				break;

			case "cinemaHalls":
				List<Map<String, Object>> halls = (List<Map<String, Object>>) value;
				System.out.println("Updating Cinema Halls: " + halls);

				for (Map<String, Object> hallUpdate : halls) {
					String hallId = (String) hallUpdate.get("id");

					CinemaHallModel updatedHall = updatedCinemaHalls.stream()
							.filter(hall -> hall.getId().equals(hallId)).findFirst().orElseGet(() -> {
								CinemaHallModel newHall = new CinemaHallModel();
								newHall.setId(hallId);
								return newHall;
							});

					if (hallUpdate.containsKey("showTimings")) {
						List<Map<String, Object>> showTimings = (List<Map<String, Object>>) hallUpdate
								.get("showTimings");

						List<ShowTimingModel> updatedShowTimings = new ArrayList<>();

						for (Map<String, Object> showUpdate : showTimings) {
							String time = (String) showUpdate.get("time");

							ShowTimingModel updatedShow = updatedHall.getShowTimings().stream()
									.filter(existingShow -> existingShow.getTime().equals(time)).findFirst()
									.orElseGet(() -> {
										ShowTimingModel newShow = new ShowTimingModel();
										newShow.setTime(time);
										return newShow;
									});

							if (showUpdate.containsKey("availableSeats")) {
								List<Map<String, Object>> seatData = (List<Map<String, Object>>) showUpdate
										.get("availableSeats");

								List<SeatModel> updatedSeats = seatData.stream().map(seat -> {
									SeatModel seatModel = new SeatModel();
									seatModel.setSeatNumber((String) seat.get("seatNumber"));
									seatModel.setSeatPrice((Integer) seat.get("seatPrice"));
									seatModel.setBooked((Boolean) seat.get("booked"));
									return seatModel;
								}).collect(Collectors.toList());

								updatedShow.setAvailableSeats(updatedSeats);
								System.out.println("Updated Seats for Show " + time + ": " + updatedSeats);
							}

							updatedShowTimings.add(updatedShow);
						}

						updatedHall.setShowTimings(updatedShowTimings);
					}

					updatedCinemaHalls.add(updatedHall);
				}

				existingMovie.setCinemaHalls(updatedCinemaHalls);
				break;
			}
		}

		System.out.println("Final Updated Movie: " + existingMovie);
		createMovieRepository.save(existingMovie);
		return ResponseEntity.ok(existingMovie);
	}

	@Override
	public void deleteMovie(String movieId) {
		createMovieRepository.deleteById(movieId);

	}

}
