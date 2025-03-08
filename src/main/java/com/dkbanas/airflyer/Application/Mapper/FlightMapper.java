package com.dkbanas.airflyer.Application.Mapper;

import com.dkbanas.airflyer.Application.RequestDTO.FlightDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.FlightResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import com.dkbanas.airflyer.Domain.Entities.Flight;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Flight | Request -> Entity, Entity -> Response
@Component
public class FlightMapper {

    // Converts a FlightDTO to a Flight entity
    public Flight toEntity(FlightDTO flightDTO, Airport departureLocation, Airport arrivalLocation) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setArrivalTime(flightDTO.getArrivalTime());
        flight.setPriceEconomy(flightDTO.getPriceEconomy());
        flight.setAirline(flightDTO.getAirline());

        flight.setCreatedAt(LocalDateTime.now());

        // Ensure both departureLocation and arrivalLocation are not null before setting
        if (departureLocation != null && arrivalLocation != null) {
            flight.setDepartureLocation(departureLocation);
            flight.setArrivalLocation(arrivalLocation);
        } else {
            throw new IllegalArgumentException("Invalid departure or arrival airport");
        }

        return flight;
    }
    // Converts a Flight entity to a FlightResponseDTO
    public FlightResponseDTO toResponseDTO(Flight flight) {
        FlightResponseDTO responseDTO = new FlightResponseDTO();
        responseDTO.setId(flight.getId());
        responseDTO.setFlightNumber(flight.getFlightNumber());

        responseDTO.setDepartureLocation(new AirportResponseDTO());
        responseDTO.getDepartureLocation().setId(flight.getDepartureLocation().getId());
        responseDTO.getDepartureLocation().setName(flight.getDepartureLocation().getName());
        responseDTO.getDepartureLocation().setCode(flight.getDepartureLocation().getCode());
        responseDTO.getDepartureLocation().setCity(flight.getDepartureLocation().getCity());
        responseDTO.getDepartureLocation().setCountry(flight.getDepartureLocation().getCountry());
        responseDTO.getDepartureLocation().setCreatedAt(flight.getDepartureLocation().getCreatedAt());
        responseDTO.getDepartureLocation().setContinent(flight.getDepartureLocation().getContinent());
        responseDTO.getDepartureLocation().setCityPhotoUrl(flight.getDepartureLocation().getCityPhotoUrl());

        responseDTO.setArrivalLocation(new AirportResponseDTO());
        responseDTO.getArrivalLocation().setId(flight.getArrivalLocation().getId());
        responseDTO.getArrivalLocation().setName(flight.getArrivalLocation().getName());
        responseDTO.getArrivalLocation().setCode(flight.getArrivalLocation().getCode());
        responseDTO.getArrivalLocation().setCity(flight.getArrivalLocation().getCity());
        responseDTO.getArrivalLocation().setCountry(flight.getArrivalLocation().getCountry());
        responseDTO.getArrivalLocation().setCreatedAt(flight.getArrivalLocation().getCreatedAt());
        responseDTO.getArrivalLocation().setContinent(flight.getArrivalLocation().getContinent());
        responseDTO.getArrivalLocation().setCityPhotoUrl(flight.getArrivalLocation().getCityPhotoUrl());

        responseDTO.setDepartureTime(flight.getDepartureTime());
        responseDTO.setArrivalTime(flight.getArrivalTime());
        responseDTO.setPriceEconomy(flight.getPriceEconomy());
        responseDTO.setTotalSeatsEconomy(flight.getTotalSeatsEconomy());
        responseDTO.setDuration(flight.getDuration());
        responseDTO.setAirline(flight.getAirline());
        responseDTO.setAvailableSeatsEconomy(flight.getAvailableSeatsEconomy());
        responseDTO.setCreatedAt(flight.getCreatedAt());

        return responseDTO;
    }
    // Converts a list of Flight entities to a list of FlightResponseDTOs
    public List<FlightResponseDTO> toResponseDTOList(List<Flight> flights) {
        return flights.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Converts a paginated list of flights objects into a paginated list of FlightResponseDTO objects
    public Page<FlightResponseDTO> toResponseDTOPage(Page<Flight> flightPage) {
        return flightPage.map(this::toResponseDTO); // Map each Flight to FlightResponseDTO
    }
}
