package com.att.tdp.popcorn_palace.repositoris;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.att.tdp.popcorn_palace.entities.Ticket;


/*uses Jpa in order to access and work with the data base */
public interface TicketRepository extends JpaRepository<Ticket, Long>{

    List<Ticket> findAllByShowtimeId(Long showtimeId);

    boolean existsByShowtimeIdAndSeatNumber(Long showtimeId, int seatNumber);

    void deleteAllByShowtimeId(Long showtimeId);
}
