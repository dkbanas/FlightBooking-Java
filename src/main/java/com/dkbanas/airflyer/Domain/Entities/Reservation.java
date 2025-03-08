package com.dkbanas.airflyer.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "reservations")
public class Reservation {
    /** Unique identifier for the reservation */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User who made the reservation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private RegisteredUser user;

    /** Flight associated with the reservation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    /** Set of reserved seat numbers */
    @ElementCollection
    @CollectionTable(name = "reserved_seats", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "seat_number")
    private Set<String> seatNumbers = new HashSet<>();

    /** Date and time of the reservation (default: current time) */
    @Column(nullable = false)
    private LocalDateTime reservationDate = LocalDateTime.now();
}
