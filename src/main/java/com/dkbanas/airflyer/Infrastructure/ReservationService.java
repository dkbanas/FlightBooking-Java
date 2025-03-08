package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.Interfaces.IFlightRepo;
import com.dkbanas.airflyer.Application.Interfaces.IReservationRepo;
import com.dkbanas.airflyer.Application.Interfaces.IReservationService;
import com.dkbanas.airflyer.Application.Interfaces.IUserRepo;
import com.dkbanas.airflyer.Application.Mapper.AirportMapper;
import com.dkbanas.airflyer.Application.Mapper.ReservationMapper;
import com.dkbanas.airflyer.Application.RequestDTO.ReservationDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.ReservationResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.RouteDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import com.dkbanas.airflyer.Domain.Entities.Flight;
import com.dkbanas.airflyer.Domain.Entities.RegisteredUser;
import com.dkbanas.airflyer.Domain.Entities.Reservation;
import com.dkbanas.airflyer.Shared.AppExceptions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {
    private final IReservationRepo reservationRepo;
    private final IFlightRepo flightRepo;
    private final IUserRepo userRepo;
    private final ReservationMapper reservationMapper;
    private final AirportMapper airportMapper;

    /* Creates a reservation for a user on a specific flight with selected seat numbers. */
    @Transactional
    @Override
    public ReservationResponseDTO createReservation(Long flightId, Integer userId, Set<String> seatNumbers) {
        Flight flight = findFlightById(flightId);
        RegisteredUser user = findUserById(userId);

        flight.reserveSeats(seatNumbers);

        Reservation reservation = reservationMapper.toEntity(
                new ReservationDTO(flightId, userId, seatNumbers), flight, user);
        reservationRepo.save(reservation);
        flightRepo.save(flight);

        return reservationMapper.toResponseDTO(reservation);
    }

    /* Cancels a reservation by its ID, releasing the reserved seats on the flight. */
    @Transactional
    @Override
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new AppExceptions.ReservationNotFoundException("Reservation not found"));

        Flight flight = reservation.getFlight();
        flight.releaseSeats(reservation.getSeatNumbers());

        reservationRepo.delete(reservation);
        flightRepo.save(flight);
    }

    /* Retrieves all reservations for a specific user by their user ID. */
    @Override
    public List<ReservationResponseDTO> getUserReservations(Integer userId) {
        List<Reservation> reservations = reservationRepo.findByUserId(userId);
        return reservationMapper.toResponseDTOList(reservations);
    }

    /* Finds a flight by its ID, throwing an exception if not found. */
    private Flight findFlightById(Long flightId) {
        return flightRepo.findById(flightId)
                .orElseThrow(() -> new AppExceptions.FlightNotFoundException("Flight not found"));
    }

    /* Finds a user by their ID, throwing an exception if not found. */
    private RegisteredUser findUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new AppExceptions.UserNotFoundException("User not found"));
    }

    /* Calculates the total profit from all reservations by summing up the revenue for each reservation. */
    @Override
    public BigDecimal calculateTotalProfit() {
        List<Reservation> reservations = reservationRepo.findAll();
        return reservations.stream()
                .map(reservation -> reservation.getFlight().getPriceEconomy()
                        .multiply(BigDecimal.valueOf(reservation.getSeatNumbers().size())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /*Return number of reservations*/
    @Override
    public long getReservationsCount() {
        return reservationRepo.count();
    }

    public Optional<RouteDTO> getMostPopularRoute() {
        List<Object[]> result = reservationRepo.findMostPopularRoute();

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Object[] row = result.get(0); // First row = most popular route
        Airport departure = (Airport) row[0];
        Airport arrival = (Airport) row[1];
        Long reservationCount = (Long) row[2];

        return Optional.of(new RouteDTO(
                airportMapper.toResponseDTO(departure),
                airportMapper.toResponseDTO(arrival),
                reservationCount
        ));
    }


}
