package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.Interfaces.IFlightRepo;
import com.dkbanas.airflyer.Application.Interfaces.IReservationRepo;
import com.dkbanas.airflyer.Application.Interfaces.IUserRepo;
import com.dkbanas.airflyer.Application.Mapper.ReservationMapper;
import com.dkbanas.airflyer.Application.RequestDTO.ReservationDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.ReservationResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Flight;
import com.dkbanas.airflyer.Domain.Entities.RegisteredUser;
import com.dkbanas.airflyer.Domain.Entities.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private IReservationRepo reservationRepo;
    @Mock
    private IFlightRepo flightRepo;
    @Mock
    private IUserRepo userRepo;
    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createReservation_ShouldReserveSeatsAndSaveReservation() {
        // Arrange
        Long flightId = 1L;
        Integer userId = 100;
        Set<String> seatNumbers = Set.of("A1", "A2");

        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setTotalSeatsEconomy(120);
        flight.setAvailableSeatsEconomy(120);
        flight.setOccupiedSeatsEconomy(new HashSet<>());

        RegisteredUser user = new RegisteredUser();
        user.setId(userId);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setFlight(flight);
        reservation.setUser(user);
        reservation.setSeatNumbers(seatNumbers);

        when(flightRepo.findById(flightId)).thenReturn(Optional.of(flight));
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(reservationMapper.toEntity(any(ReservationDTO.class), any(Flight.class), any(RegisteredUser.class)))
                .thenReturn(reservation);
        when(reservationRepo.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponseDTO(any(Reservation.class))).thenReturn(new ReservationResponseDTO());

        // Act
        ReservationResponseDTO result = reservationService.createReservation(flightId, userId, seatNumbers);

        // Assert
        assertNotNull(result);
        assertEquals(118, flight.getAvailableSeatsEconomy());
        assertTrue(flight.getOccupiedSeatsEconomy().containsAll(seatNumbers));

        verify(flightRepo).save(flight);
        verify(reservationRepo).save(reservation);
    }

    @Test
    void cancelReservation_ShouldReleaseSeatsAndDeleteReservation() {
        // Arrange
        Long reservationId = 1L;
        Set<String> seatNumbers = Set.of("A1", "A2");

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeatsEconomy(118);
        flight.setOccupiedSeatsEconomy(new HashSet<>(seatNumbers));

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setFlight(flight);
        reservation.setSeatNumbers(seatNumbers);

        when(reservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        reservationService.cancelReservation(reservationId);

        // Assert
        assertEquals(120, flight.getAvailableSeatsEconomy());
        assertFalse(flight.getOccupiedSeatsEconomy().containsAll(seatNumbers));

        verify(flightRepo).save(flight);
        verify(reservationRepo).delete(reservation);
    }

    @Test
    void getUserReservations_ShouldReturnReservationList() {
        // Arrange
        Integer userId = 100;
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        List<ReservationResponseDTO> responseDTOs = List.of(new ReservationResponseDTO(), new ReservationResponseDTO());

        when(reservationRepo.findByUserId(userId)).thenReturn(reservations);
        when(reservationMapper.toResponseDTOList(reservations)).thenReturn(responseDTOs);

        // Act
        List<ReservationResponseDTO> result = reservationService.getUserReservations(userId);

        // Assert
        assertEquals(2, result.size());
        verify(reservationRepo).findByUserId(userId);
        verify(reservationMapper).toResponseDTOList(reservations);
    }
}