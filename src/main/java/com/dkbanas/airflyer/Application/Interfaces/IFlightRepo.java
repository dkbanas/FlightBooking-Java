package com.dkbanas.airflyer.Application.Interfaces;

import com.dkbanas.airflyer.Domain.Entities.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IFlightRepo extends JpaRepository<Flight, Long> {

    /**Retrieves all flights with pagination.*/
    Page<Flight> findAll(Pageable pageable);

    /**Finds a flight by its unique flight number.*/
    Optional<Flight> findByFlightNumber(String flightNumber);

    /**Finds available flights based on departure, arrival, date range, and seat availability.*/
    @Query("SELECT f FROM Flight f WHERE f.departureLocation.id = :departureLocationId " +
            "AND f.arrivalLocation.id = :arrivalLocationId " +
            "AND f.departureTime BETWEEN :startOfDay AND :endOfDay " +
            "AND f.availableSeatsEconomy >= :numberOfPassengers")
    List<Flight> findAvailableFlights(
            @Param("departureLocationId") Long departureLocationId,
            @Param("arrivalLocationId") Long arrivalLocationId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("numberOfPassengers") int numberOfPassengers
    );

    /**Counts flights grouped by departure location.*/
    @Query("SELECT f.departureLocation, COUNT(f) FROM Flight f GROUP BY f.departureLocation")
    List<Object[]> countFlightsByDeparture();

    /**Counts flights grouped by arrival location.*/
    @Query("SELECT f.arrivalLocation, COUNT(f) FROM Flight f GROUP BY f.arrivalLocation")
    List<Object[]> countFlightsByArrival();

    /** Finds the cheapest flight based on economy class price. */
    Flight findTopByOrderByPriceEconomyAsc();

    /** Finds the most expensive flight based on economy class price. */
    Flight findTopByOrderByPriceEconomyDesc();


}
