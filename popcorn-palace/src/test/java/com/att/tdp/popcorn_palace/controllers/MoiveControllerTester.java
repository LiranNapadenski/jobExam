package com.att.tdp.popcorn_palace.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.repositoris.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MovieController.class)
public class MoiveControllerTester {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = new Movie();
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDuration(148);
        movie.setRating(8.8f);
        movie.setReleaseYear(2010);
    }

    @Test
    void testGetAllMovies() throws Exception {
        // Simulate database return
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        mockMvc.perform(get("/movies/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Inception"))
            .andExpect(jsonPath("$[0].genre").value("Sci-Fi"));
    }

    @Test
    void testPostMovie() throws Exception {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/movies")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(movie)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Inception"))
            .andExpect(jsonPath("$.rating").value(8.8f));
    }

    @Test
    void testUpdateMovie() throws Exception {
        when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(movie));
        movie.setDuration(150);

        mockMvc.perform(post("/movies/update/Inception")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(movie)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.duration").value(150));
    }

    @Test
    void testDeleteMovie() throws Exception {
        when(movieRepository.existsByTitle("Inception")).thenReturn(true);

        mockMvc.perform(delete("/movies/Inception"))
            .andExpect(status().isOk());
    }

    @Test
    void testDeleteMovieNotFound() throws Exception {
        when(movieRepository.existsByTitle("Inception")).thenReturn(false);

        mockMvc.perform(delete("/movies/Inception"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("There is no movie by this title"));
    }

    @Test
    void testPostMovieValidationError() throws Exception {
        // Test with invalid data (e.g., rating out of bounds)
        movie.setRating(15.0f);

        mockMvc.perform(post("/movies")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(movie)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors").exists());
    }
}

