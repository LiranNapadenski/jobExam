package com.att.tdp.popcorn_palace.repositoris;

import org.springframework.data.jpa.repository.JpaRepository;

import com.att.tdp.popcorn_palace.entities.Showtime;

/*uses Jpa in order to access and work with the data base */
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

}
