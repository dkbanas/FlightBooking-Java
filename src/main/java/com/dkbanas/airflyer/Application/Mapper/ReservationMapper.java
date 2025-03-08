package com.dkbanas.airflyer.Application.Mapper;

import com.dkbanas.airflyer.Application.RequestDTO.ReservationDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.ReservationResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import com.dkbanas.airflyer.Domain.Entities.Flight;
import com.dkbanas.airflyer.Domain.Entities.RegisteredUser;
import com.dkbanas.airflyer.Domain.Entities.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Reservation | Request -> Entity, Entity -> Response
@Component
public class ReservationMapper {

    // Converts a ReservationDTO to a Reservation entity
    public Reservation toEntity(ReservationDTO dto, Flight flight, RegisteredUser user) {
        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setUser(user);
        reservation.setSeatNumbers(dto.getSeatNumbers());
        reservation.setReservationDate(LocalDateTime.now());
        return reservation;
    }

    // Converts a Reservation entity to a ReservationResponseDTO
    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setFlightNumber(reservation.getFlight().getFlightNumber());
        dto.setUserEmail(reservation.getUser().getEmail());
        dto.setSeatNumbers(reservation.getSeatNumbers());
        dto.setReservationDate(reservation.getReservationDate());

        Flight flight = reservation.getFlight();
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setPriceEconomy(flight.getPriceEconomy());

        Airport departureLocation = flight.getDepartureLocation();
        dto.setDepartureName(departureLocation.getName());
        dto.setDepartureCity(departureLocation.getCity());
        dto.setDepartureCountry(departureLocation.getCountry());

        Airport arrivalLocation = flight.getArrivalLocation();
        dto.setArrivalName(arrivalLocation.getName());
        dto.setArrivalCity(arrivalLocation.getCity());
        dto.setArrivalCountry(arrivalLocation.getCountry());

        return dto;
    }

    // Converts a list of Reservation entities to a list of ReservationResponseDTOs
    public List<ReservationResponseDTO> toResponseDTOList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

}
