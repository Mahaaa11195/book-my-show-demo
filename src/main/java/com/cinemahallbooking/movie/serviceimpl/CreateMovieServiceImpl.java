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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateMovieServiceImpl implements CreateMovieService {

	@Autowired
	private CreateMovieRepository createMovieRepository;

	@Autowired
	private CinemaHallRepository cinemaHallRepository;

	@Autowired
	private LocationsRepository locationRepository;

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

		// Fetch cinemaHalls based on the provided cinemaHall IDs
		List<String> cinemaHallIds = request.getCinemaHalls().stream().map(CinemaHallModel::getId)
				.collect(Collectors.toList());
		List<CinemaHallModel> cinemaHalls = cinemaHallRepository.findAllById(cinemaHallIds);

		// Check if a movie event already exists with the same releaseDate, location,
		// cinemaHall, and showTiming
		for (CinemaHallModel reqCinemaHall : request.getCinemaHalls()) {
			for (ShowTimingModel reqShowTime : reqCinemaHall.getShowTimings()) {
				boolean isDuplicate = createMovieRepository
						.existsByReleaseDateAndLocations_IdAndCinemaHalls_IdAndCinemaHalls_ShowTimings_Time(
								request.getReleaseDate(), locationIds, reqCinemaHall.getId(), reqShowTime.getTime());

				if (isDuplicate) {
					return new ResponseEntity<>(
							"A movie event already exists for this date, location, cinema hall, and show timing!",
							HttpStatus.CONFLICT);
				}
			}
		}

		// Generate seats for valid cinema halls
		for (CinemaHallModel cinemaHall : cinemaHalls) {
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
		CreateMovieModel newMovie = new CreateMovieModel(request, locations, cinemaHalls);
		createMovieRepository.save(newMovie);

		return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
	}


	/**
	 * Generates a list of seats based on total seats.
	 */
	private List<SeatModel> generateSeats(int totalSeats) {
		List<SeatModel> seats = new ArrayList<>();
		for (int i = 1; i <= totalSeats; i++) {
			seats.add(new SeatModel("A" + i, i <= 2 ? 200 : 150));
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
			movie.setCinemaHalls(fullCinemaHalls);
		});

		return movies;
	}

	public ResponseEntity<?> updateMovie(String movieId, CreateMovieModel updatedMovie) {
		Optional<CreateMovieModel> existingMovieOpt = createMovieRepository.findById(movieId);
		if (existingMovieOpt.isEmpty()) {
			return new ResponseEntity<>("Movie not found!", HttpStatus.NOT_FOUND);
		}

		CreateMovieModel existingMovie = existingMovieOpt.get();

		existingMovie.setMovieTitle(updatedMovie.getMovieTitle());
		existingMovie.setGenre(updatedMovie.getGenre());
		existingMovie.setImage(updatedMovie.getImage());
		existingMovie.setReleaseDate(updatedMovie.getReleaseDate());

		if (updatedMovie.getReleaseDate() != null) {
			existingMovie.setReleaseEndDate(updatedMovie.getReleaseDate().plusDays(6));
		}

		// Fetch updated locations
		List<String> locationIds = updatedMovie.getLocations().stream().map(LocationModel::getId)
				.collect(Collectors.toList());

		List<LocationModel> locations = locationRepository.findAllById(locationIds);
		existingMovie.setLocations(locations);

		// Fetch updated cinemahalls
		List<String> cinemaHallsIds = updatedMovie.getCinemaHalls().stream().map(CinemaHallModel::getId)
				.collect(Collectors.toList());

		List<CinemaHallModel> cinemaHalls = cinemaHallRepository.findAllById(cinemaHallsIds);
		existingMovie.setCinemaHalls(cinemaHalls);
		for (CinemaHallModel cinemaHall : cinemaHalls) {
			updatedMovie.getCinemaHalls().stream().filter(reqCinema -> reqCinema.getId().equals(cinemaHall.getId()))
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
		CreateMovieModel updatedExistingMovie = new CreateMovieModel(updatedMovie, locations, cinemaHalls);
		// Regenerate Movie Schedule after updates
		updatedExistingMovie.updateMovieSchedule();

		// Save the updated movie
		createMovieRepository.save(updatedExistingMovie);

		return new ResponseEntity<>(updatedExistingMovie, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<?> updateMovie1(String movieId, Map<String, Object> updates) {
		Optional<CreateMovieModel> existingMovieOpt = createMovieRepository.findById(movieId);
		if (existingMovieOpt.isEmpty()) {
			return new ResponseEntity<>("Movie not found!", HttpStatus.NOT_FOUND);
		}

		CreateMovieModel existingMovie = existingMovieOpt.get();
		System.out.println("Received Updates: " + updates);

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
//			case "image":
//				existingMovie.setImage((String) value);
//				break;
			case "releaseDate":
				LocalDate newDate = LocalDate.parse((String) value);
				existingMovie.setReleaseDate(newDate);
				existingMovie.setReleaseEndDate(newDate.plusDays(6));
				break;
			case "locations":
				List<String> locationIds = ((List<Map<String, String>>) value).stream().map(loc -> loc.get("id"))
						.collect(Collectors.toList());
				updatedLocations.clear();
				updatedLocations.addAll(locationRepository.findAllById(locationIds));
				existingMovie.setLocations(updatedLocations);

				// Filter cinema halls dynamically based on updated locations
				List<CinemaHallModel> allCinemaHalls = cinemaHallRepository.findAll();
				updatedCinemaHalls.clear();
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
