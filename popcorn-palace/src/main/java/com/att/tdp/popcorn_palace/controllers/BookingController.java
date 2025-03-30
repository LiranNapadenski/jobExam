package com.att.tdp.popcorn_palace.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.controllers.utils.BookingUtils;
import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.entities.Ticket;
import com.att.tdp.popcorn_palace.repositoris.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repositoris.TicketRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



/*controller for all the booking */
@AllArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;

    /*the post method including all the neccessery validations */
    @PostMapping("")
    public ResponseEntity<?> postBooking(@RequestBody @Valid Ticket ticket) {
        Optional<Showtime> showtime = showtimeRepository.findById(ticket.getShowtimeId());
        if(showtime.isEmpty()){//validates if the showtime exists
            return ResponseEntity.badRequest().body("sorry the showtime does not exist");
        }

        if(ticketRepository.existsByShowtimeIdAndSeatNumber(ticket.getShowtimeId(), ticket.getSeatNumber())){//checks if the seat is already taken
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Sorry, the seat is already taken");
        }
        
        ticket.setMovieId(showtime.get().getMovieId());
        ticketRepository.save(ticket);
        Long ticket_id = ticket.getId();
        String bookingId = BookingUtils.generateBookingId(ticket_id);
        return ResponseEntity.ok().body(Map.of("bookingId", bookingId));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, List<Ticket>>> getAllTicketsGroupedByShowtime() {
        // Retrieve all tickets from the repository
        List<Ticket> allTickets = ticketRepository.findAll();

        // Group tickets by their showtimeId
        Map<String, List<Ticket>> ticketsGroupedByShowtime = allTickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getShowtimeId().toString()));

        return ResponseEntity.ok(ticketsGroupedByShowtime);
    }
    
    
}
