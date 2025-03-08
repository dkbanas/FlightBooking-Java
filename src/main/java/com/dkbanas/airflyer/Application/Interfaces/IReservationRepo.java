package com.dkbanas.airflyer.Application.Interfaces;

import com.dkbanas.airflyer.Domain.Entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IReservationRepo extends JpaRepository<Reservation, Long> {
    /**Finds reservations by user ID.*/
    List<Reservation> findByUserId(Integer userId);

    /**Finds reserved seat numbers for a given flight.*/
    @Query("SELECT r.seatNumbers FROM Reservation r WHERE r.flight.id = :flightId")
    List<Set<String>> findSeatNumbersByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT f.departureLocation, f.arrivalLocation, COUNT(r.id) AS reservationCount " +
            "FROM Reservation r " +
            "JOIN r.flight f " +
            "GROUP BY f.departureLocation, f.arrivalLocation " +
            "ORDER BY reservationCount DESC")
    List<Object[]> findMostPopularRoute();

}
