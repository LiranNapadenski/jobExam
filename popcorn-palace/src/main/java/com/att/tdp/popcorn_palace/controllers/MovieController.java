package com.att.tdp.popcorn_palace.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.att.tdp.popcorn_palace.entities.Movie;
import com.att.tdp.popcorn_palace.repositoris.MovieRepository;
import jakarta.validation.Valid;



@RequestMapping("/movies")
@RestController
public class MovieController {
    
    private final MovieRepository movieRepository;//repository for database access

    public MovieController(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    /*find all the movies and returns them*/
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieRepository.findAll().stream().toList();
        return ResponseEntity.ok(movies);
    }

    /*posts a new movie using the repository and validates the data using jakarta.validation and the constraints specified in the entity creation */
    @PostMapping("")
    public ResponseEntity<Movie> postMovie(@Valid @RequestBody Movie movie) {
        movieRepository.save(movie);
        return ResponseEntity.ok(movie);
    }

    /*searches the movie using the title and updates its properties
     * after its validates the request body using @valid
    */
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<?> updateByTitle(@PathVariable String movieTitle, @Valid @RequestBody Movie movie) {
        Optional<Movie> suspectMovie = movieRepository.findByTitle(movieTitle);
        if(suspectMovie.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The movie you are tring to update does not exists");
        }
        
        Movie savedmovie = suspectMovie.get();
        savedmovie.setDuration(movie.getDuration());
        savedmovie.setGenre(movie.getGenre());
        savedmovie.setRating(movie.getRating());
        savedmovie.setReleaseYear(movie.getReleaseYear());
        savedmovie.setTitle(movie.getTitle());
        movieRepository.save(savedmovie);

        return ResponseEntity.ok().build();
    }

    /*searches the movie by title - if not found returns an error
     * else delete all the movies with this title
     */
    @Transactional
    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<String> deleteByTitle(@PathVariable String movieTitle){
        if(!movieRepository.existsByTitle(movieTitle)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no movie by this title");
        }
        movieRepository.deleteAllByTitle(movieTitle);
        return ResponseEntity.ok().build();
    }

    
    

}
