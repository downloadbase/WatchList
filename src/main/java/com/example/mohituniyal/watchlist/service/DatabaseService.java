package com.example.mohituniyal.watchlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mohituniyal.watchlist.entity.Movie;
import com.example.mohituniyal.watchlist.repository.MovieRepo;

@Service
public class DatabaseService {

	@Autowired
	MovieRepo movierepo;
	
	@Autowired
	movieRating movierating;
	
	public void Create(Movie movie) {
		
		String rating = movierating.getMovieRating(movie.getTitle());
		
		if(rating!=null) {
			movie.setRating(Float.parseFloat(rating));
		}
		movierepo.save(movie);
	}
	
	//This method will return the list of all movies
	public List<Movie> getAllMovies(){
		return movierepo.findAll();
	}
	
	public Movie getMovieById(Integer id) {
		return movierepo.findById(id).get();
	}

	public void update(Movie movie, Integer id) {
	    // Fetch the existing movie
	    Movie movieToBeUpdated = getMovieById(id);
	    
	    // Check if the rating field is empty or the user has tried to change it
	    if (movie.getRating() != 0) {
	        movieToBeUpdated.setRating(movie.getRating()); // Update rating if provided
	    }
	    movieToBeUpdated.setTitle(movie.getTitle());
	    movieToBeUpdated.setPriority(movie.getPriority());
	    movieToBeUpdated.setComment(movie.getComment());
	    
	    // Save the updated movie
	    movierepo.save(movieToBeUpdated);
	}


}