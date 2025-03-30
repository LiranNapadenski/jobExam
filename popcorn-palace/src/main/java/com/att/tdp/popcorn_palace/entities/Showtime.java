package com.att.tdp.popcorn_palace.entities;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="showtimes")
@Entity
/*this class represents the Shotime resource and includes some validations for the fields */
public class Showtime {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "requests must contain theater")
    @Size(max=50, message="theater name is too theater")
    @Column(name="theater", nullable=false)
    private String theater;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "requests must contain start_time")
    @Column(name="start_time", nullable=false)
    private OffsetDateTime start_time;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "requests must contain end_time")
    @Column(name="end_time", nullable=false)
    private OffsetDateTime end_time;

    @NotNull(message = "requests must contain price")
    @Positive(message="price must me positive")
    @Column(name="price", nullable=false)
    private float price;

    @NotNull(message = "requests must contain movieId")
    @Column(name="movie_id",  nullable=false)
    private Long movieId;



    @AssertTrue(message = "End time must be after start time")
    public boolean isEndAfterStart() {
        return end_time != null && start_time != null && end_time.isAfter(start_time);
    }

}