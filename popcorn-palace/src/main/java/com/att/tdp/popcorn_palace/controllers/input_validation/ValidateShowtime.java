package com.att.tdp.popcorn_palace.controllers.input_validation;

import org.springframework.stereotype.Component;

import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.repositoris.ShowtimeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ValidateShowtime {
    private final ShowtimeRepository showtimeRepository;
    
    public boolean isLegalTimeFrame(Showtime showtime) {
        return showtimeRepository.findAll().stream()
            .noneMatch(existingShowtime ->
                existingShowtime.getTheater().equals(showtime.getTheater()) &&
                (showtime.getId() == null || !existingShowtime.getId().equals(showtime.getId())) &&
                (
                    (showtime.getStart_time().isAfter(existingShowtime.getStart_time()) 
                    && showtime.getStart_time().isBefore(existingShowtime.getEnd_time())) ||
    
                    (showtime.getEnd_time().isAfter(existingShowtime.getStart_time()) 
                    && showtime.getEnd_time().isBefore(existingShowtime.getEnd_time())) ||
    
                    (showtime.getStart_time().equals(existingShowtime.getStart_time()) ||
                    showtime.getEnd_time().equals(existingShowtime.getEnd_time()))
                )
            );
    }
}
