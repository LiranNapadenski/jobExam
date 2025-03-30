package com.att.tdp.popcorn_palace.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.att.tdp.popcorn_palace.controllers.input_validation.ValidateShowtime;
import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.repositoris.MovieRepository;
import com.att.tdp.popcorn_palace.repositoris.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repositoris.TicketRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeRepository showtimeRepository;//showtime repo for handling showtime table
    private final MovieRepository movieRepository;//movie repo for handling movies table
    private final TicketRepository ticketRepository;
    private final ValidateShowtime validateShowtime;// a class that includes some input checks

    /*gets shotime bpject by id */
    @GetMapping("/{showtimeid}")
    public ResponseEntity<Showtime> getById(@PathVariable Long showtimeid) {
        return showtimeRepository.findById(showtimeid).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /*deletes showtime object by id */
    @Transactional
    @DeleteMapping("/{showtimeid}")
    public ResponseEntity<?> deleteById(@PathVariable Long showtimeid) {
        if (showtimeRepository.existsById(showtimeid)) {
            ticketRepository.deleteAllByShowtimeId(showtimeid);
            showtimeRepository.deleteById(showtimeid);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Showtime with ID " + showtimeid + " not found.");
    }

    /*post showtime */
    @PostMapping("")
    public ResponseEntity<?> postShowtime(@Valid @RequestBody Showtime showtime) {
        if (!movieRepository.existsById(showtime.getMovieId())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Movie with ID " + showtime.getMovieId() + " does not exist.");
        }
        if(!validateShowtime.isLegalTimeFrame(showtime)){//checks if the new time frame of the show is independent of the others in the same theather
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("there is already another show taking palce in the given time frame");
        }
        showtimeRepository.save(showtime);
        return ResponseEntity.ok(showtime);
    }

    /*updates a showtime and includes all the necessery validations */
    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<?> updateShowtime(@Valid @RequestBody Showtime showtime, @PathVariable Long showtimeId) {
        
        Optional<Showtime> OsavedShowtime = showtimeRepository.findById(showtimeId);
        if(OsavedShowtime.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Could not update because the Showtime does'nt esxists");
        }
        if (!movieRepository.existsById(showtime.getMovieId())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Movie with ID " + showtime.getMovieId() + " does not exist.");
        }



        Showtime savedShowtime = OsavedShowtime.get();
        savedShowtime.setStart_time(showtime.getStart_time());
        savedShowtime.setEnd_time(showtime.getEnd_time());
        savedShowtime.setMovieId(showtime.getMovieId());
        savedShowtime.setTheater(showtime.getTheater());
        savedShowtime.setPrice(showtime.getPrice());
        if(!validateShowtime.isLegalTimeFrame(savedShowtime)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("there is already another show taking palce in the given time frame");
        }
        showtimeRepository.save(savedShowtime);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/showtimes/grouped-by-theater")
    public ResponseEntity<Map<String, List<Showtime>>> getShowtimesGroupedByTheater() {
        List<Showtime> showtimes = showtimeRepository.findAll();
        
        Map<String, List<Showtime>> groupedShowtimes = showtimes.stream()
            .collect(Collectors.groupingBy(Showtime::getTheater));
        
        return ResponseEntity.ok(groupedShowtimes);
    }
    
    

}
