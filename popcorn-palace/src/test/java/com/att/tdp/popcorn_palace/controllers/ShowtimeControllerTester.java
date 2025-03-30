package com.att.tdp.popcorn_palace.controllers;

import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.repositoris.ShowtimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@WebMvcTest(ShowtimeController.class)
public class ShowtimeControllerTester{

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private ShowtimeController showtimeController;

    private Showtime showtime;

    @BeforeEach
    void setUp() {
        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setTheater("IMAX");
        showtime.setStart_time(LocalDateTime.of(2025, 3, 30, 14, 0).atOffset(ZoneOffset.UTC));
        showtime.setEnd_time(LocalDateTime.of(2025, 3, 30, 16, 0).atOffset(ZoneOffset.UTC));
        showtime.setPrice(15.0f);
        showtime.setMovieId(1L);
    }

    @Test
    void testDeleteShowtime_Exists() throws Exception {
        when(showtimeRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/showtimes/1"))
                .andExpect(status().isOk());
        verify(showtimeRepository).deleteById(1L);
    }

    @Test
    void testDeleteShowtime_NotFound() throws Exception {
        when(showtimeRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/showtimes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteShowtime_InternalServerError() throws Exception {
        when(showtimeRepository.existsById(1L)).thenThrow(new RuntimeException("Internal Error"));

        mockMvc.perform(delete("/showtimes/1"))
                .andExpect(status().isInternalServerError());
    }
}
