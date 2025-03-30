package com.att.tdp.popcorn_palace.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="movies")
@Entity
/*This class represents the movie entity */
public class Movie {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "requests must contain title")
    @Size(max=30, message="your title is a bit too long")
    @Column(name="title", nullable=false)
    private String title;

    @NotNull(message = "requests must contain genre")
    @Size(max=30, message="your genere is a bit too long")
    @Column(name="genere", nullable=false)
    private String genre;

    @NotNull(message = "requests must contain duration")
    @Positive(message="duration must to be positive")
    @Column(name="duration", nullable=false)
    private int duration;

    @NotNull(message = "requests must contain rating")
    @Positive(message="rating must to be positive")
    @Max(value = 10, message = "in my site the largest rating possible is 10")
    @Column(name="rating", nullable=false)
    private float rating;

    @NotNull(message = "requests must contain releaseYear")
    @Min(value = 1888, message = "Movies were not produced before 1888")
    @Column(name="release_year", nullable=false)
    private int releaseYear;
}
