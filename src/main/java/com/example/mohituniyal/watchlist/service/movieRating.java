package com.example.mohituniyal.watchlist.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class movieRating {

    String apiurl = "https://www.omdbapi.com/?apikey=2c188e38&t="; 
    					

    public String getMovieRating(String title) {
        try {
            // Fetching movie rating from OMDb API
            RestTemplate template = new RestTemplate();
            ResponseEntity<ObjectNode> response = template.getForEntity(apiurl + title, ObjectNode.class);
            ObjectNode jsonObject = response.getBody();

            //"Response": This is a special field returned by the OMDb API. It will be "True" if the movie is found, and "False" if not.
            if (jsonObject != null && jsonObject.has("Response") && jsonObject.get("Response").asText().equals("True")) {
                // Movie found, return the IMDb rating
                return jsonObject.path("imdbRating").asText();
            } else {
                // Movie not found in OMDb
                System.out.println("Movie not found in OMDb: " + title);
                return "Movie not found in OMDb";
            }

        } catch (Exception e) {
            // Detailed logging of the error
            System.out.println("Error occurred while fetching the movie rating: " + e.getMessage());
            e.printStackTrace();  // Prints full stack trace for debugging
            return "Error: Could not fetch rating";
        }
    }
}
