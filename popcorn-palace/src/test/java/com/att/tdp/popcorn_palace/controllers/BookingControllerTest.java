package com.att.tdp.popcorn_palace.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.att.tdp.popcorn_palace.entities.Showtime;
import com.att.tdp.popcorn_palace.entities.Ticket;
import com.att.tdp.popcorn_palace.repositoris.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repositoris.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void shouldReturnBadRequestWhenShowtimeDoesNotExist() throws Exception {
        Ticket ticket = Ticket.builder()
                .seatNumber(10)
                .userId("user123456789")
                .showtimeId(1L)
                .build();

        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnConflictWhenSeatIsAlreadyTaken() throws Exception {
        Ticket ticket = Ticket.builder()
                .seatNumber(10)
                .userId("user123456789")
                .showtimeId(1L)
                .build();

        Showtime showtime = Showtime.builder()
                .id(1L)
                .movieId(100L)
                .build();

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.existsByShowtimeIdAndSeatNumber(1L, 10)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnOkWhenBookingIsSuccessful() throws Exception {
        Ticket ticket = Ticket.builder()
                .seatNumber(10)
                .userId("user123456789")
                .showtimeId(1L)
                .build();

        Showtime showtime = Showtime.builder()
                .id(1L)
                .movieId(100L)
                .build();

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.existsByShowtimeIdAndSeatNumber(1L, 10)).thenReturn(false);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket savedTicket = invocation.getArgument(0);
            savedTicket.setId(100L);
            return savedTicket;
        });

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").exists());
    }

    @Test
    void shouldReturnGroupedTicketsByShowtime() throws Exception {
        Ticket ticket1 = Ticket.builder()
                .id(1L)
                .seatNumber(10)
                .userId("user123456789")
                .showtimeId(1L)
                .build();

        Ticket ticket2 = Ticket.builder()
                .id(2L)
                .seatNumber(15)
                .userId("user123456789")
                .showtimeId(1L)
                .build();

        Ticket ticket3 = Ticket.builder()
                .id(3L)
                .seatNumber(5)
                .userId("user987654321")
                .showtimeId(2L)
                .build();

        when(ticketRepository.findAll()).thenReturn(List.of(ticket1, ticket2, ticket3));

        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['1']").isArray())
                .andExpect(jsonPath("$.['2']").isArray());
    }
}
