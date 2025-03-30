package com.att.tdp.popcorn_palace.entities;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="tickets")
@Entity
/*this class represents the ticket resource which will be used for booking*/
public class Ticket {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "requests must contain seatNumber")
    @Positive(message = "there is no negative seat number")
    @Max(value=500, message = "My theaters only have 500 seats")
    @Column(name="seat_number", nullable=false)
    private int seatNumber;
    
    @NotNull(message = "requests must contain userId")
    @Size(max = 200, message = "your user id cant be that long")
    @Size(min = 10, message = "your user id cant be too short")
    @Column(name="user_id", nullable=false)
    private String userId;

    @NotNull(message = "requests must contain showtimeId")
    @Column(name="Shwotime_id", nullable=false)
    private Long showtimeId;

    @ToString.Exclude
    @Column(name="movie_id_son", nullable=false)
    private Long movieId;
}
