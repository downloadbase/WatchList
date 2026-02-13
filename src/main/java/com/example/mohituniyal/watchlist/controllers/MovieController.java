package com.example.mohituniyal.watchlist.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.mohituniyal.watchlist.entity.Movie;
import com.example.mohituniyal.watchlist.service.DatabaseService;
import com.example.mohituniyal.watchlist.service.movieRating;

import jakarta.validation.Valid;

@RestController
public class MovieController {
	
	@Autowired
	DatabaseService databseservice;
	
	@Autowired
	movieRating movierating;

	@GetMapping("/watchlistreview")
	public ModelAndView showWatchlistForm(@RequestParam(required = false) Integer id) {
		String viewName = "watchlistreview";
		Map<String,Object> model = new HashMap<>();
		//If requestParameter (id) == null so we will have to enter new movie
		if(id==null) {
		model.put("watchlistItem", new Movie());
		}
//		Movie obj = new Movie();
//		obj.setTitle("Dummy");
//		obj.setPriority("Medium");
//		obj.setComment("OKok");
//		model.put("watchlistItem" , obj);
		
		//else old movie to update..So it will be automatically filled as we are finding the movie by id and sending it as a model in a form
		else {
			model.put("watchlistItem", databseservice.getMovieById(id));
		}
		return new ModelAndView(viewName, model);
	
	}
	
	 @PostMapping("/watchlistreview")
	    public ModelAndView createWatchlistItem(@Valid @ModelAttribute("watchlistItem") Movie movie, BindingResult bindingresult) {

	        if (bindingresult.hasErrors()) {
	            return new ModelAndView("watchlistreview");
	        }
	        
	        // Fetch the rating from OMDb API if it's a new movie
	        if (movie.getId() == null) {
	            String rating = movierating.getMovieRating(movie.getTitle());
	            
	            // Check if the movie is not found in OMDb
	            if (rating == null || rating.equals("Movie not found in OMDb")) {
	                ModelAndView mav = new ModelAndView("watchlistreview");
	                mav.addObject("error", "Movie not found in OMDb. Please enter a valid movie name.");
	                return mav;
	            }

	            try {
	                movie.setRating(Float.parseFloat(rating));
	            } catch (NumberFormatException e) {
	                ModelAndView mav = new ModelAndView("watchlistreview");
	                mav.addObject("error", "Invalid rating format. Please try again.");
	                return mav;
	            }
	        }
	        
	        // If the movie is being updated
	        Integer id = movie.getId();
	        if (id == null) {
	            databseservice.Create(movie);
	        } else {
	            databseservice.update(movie, id);
	        }

	        RedirectView rd = new RedirectView();
	        rd.setUrl("/watchlist");
	        return new ModelAndView(rd);
	    }
	
	@GetMapping("/watchlist")
	public ModelAndView getWatchlistItem() {
		
		String viewName = "watchlist";
		
		Map<String,Object> model = new HashMap<>();
		//It will fetch the list of movies
		List<Movie> movieList = databseservice.getAllMovies();
		//This will the the list of all movie by the method made in DatabseService class
		//We have to make  the Movie's row dynamic hence the variable name is watchlistrows
		model.put("watchlistrows", databseservice.getAllMovies());
		model.put("noofmovies", movieList.size());
		return new ModelAndView(viewName,model);
	}
}