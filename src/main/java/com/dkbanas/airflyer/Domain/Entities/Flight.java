package com.dkbanas.airflyer.Domain.Entities;


import com.dkbanas.airflyer.Shared.AppExceptions;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "flights")
public class Flight {
    /** Unique identifier for the flight */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique flight number, required */
    @Column(unique = true,nullable = false)
    private String flightNumber;

    /** Departure airport */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_location_id",referencedColumnName = "id", nullable = false)
    private Airport departureLocation;

    /** Arrival airport */
    @ManyToOne
    @JoinColumn(name = "arrival_location_id", referencedColumnName = "id", nullable = false)
    private Airport arrivalLocation;

    /** Departure date and time */
    private LocalDateTime departureTime;

    /** Arrival date and time */
    private LocalDateTime arrivalTime;

    /** Economy class ticket price */
    private BigDecimal priceEconomy;

    /** Total seats available in economy class (default: 120) */
    private int totalSeatsEconomy = 120;

    /** Available economy class seats */
    private int availableSeatsEconomy = 120;

    /** Flight duration */
    private String duration;

    /** Airline operating the flight */
    private String airline;

    /** Timestamp when the record was created */
    private LocalDateTime createdAt;

    /** Set of occupied economy class seats */
    @ElementCollection
    @CollectionTable(name = "occupied_seats_economy", joinColumns = @JoinColumn(name = "flight_id"))
    @Column(name = "seat")
    private Set<String> occupiedSeatsEconomy = new HashSet<>();

    /**
     * Reserves selected seats for the flight.
     * @param seatNumbers Set of seat numbers to be reserved
     */
    public void reserveSeats(Set<String> seatNumbers) {
        if (!isSeatsAvailable(seatNumbers)) {
            throw new AppExceptions.SeatAlreadyTakenException("Some selected seats are already taken.");
        }
        occupiedSeatsEconomy.addAll(seatNumbers);
        availableSeatsEconomy -= seatNumbers.size();
    }

    /**
     * Releases reserved seats.
     * @param seatNumbers Set of seat numbers to be released
     */
    public void releaseSeats(Set<String> seatNumbers) {
        occupiedSeatsEconomy.removeAll(seatNumbers);
        availableSeatsEconomy += seatNumbers.size();
    }

    /**
     * Checks if the selected seats are available.
     * @param seatNumbers Set of seat numbers to check
     * @return true if all seats are available, false otherwise
     */
    public boolean isSeatsAvailable(Set<String> seatNumbers) {
        return seatNumbers.stream().noneMatch(occupiedSeatsEconomy::contains);
    }
}