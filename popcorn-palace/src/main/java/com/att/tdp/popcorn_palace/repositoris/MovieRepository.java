package com.att.tdp.popcorn_palace.repositoris;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.att.tdp.popcorn_palace.entities.Movie;

/*uses Jpa in order to access and work with the data base */
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);

    boolean existsByTitle(String title);

    void deleteAllByTitle(String title);
}
