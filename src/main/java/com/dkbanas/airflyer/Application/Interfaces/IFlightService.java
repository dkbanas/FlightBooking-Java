package com.dkbanas.airflyer.Application.Interfaces;

import com.dkbanas.airflyer.Application.RequestDTO.FlightDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.FlightResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.RoundTripFlightResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Flight;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IFlightService {
    List<FlightResponseDTO> getAllFlights();
    Page<FlightResponseDTO> getFlightsPaginatedAndSorted(int page, int size, String sortBy, String sortDirection);
    FlightResponseDTO getFlightByFlightNumber(String flightNumber);
    FlightResponseDTO createFlight(FlightDTO request);
    void deleteFlight(String flightNumber);
    List<RoundTripFlightResponseDTO> searchFlights(Long departureLocationId, Long arrivalLocationId, LocalDate departureDate, LocalDate returnDate, int numberOfPassengers, boolean roundTrip);
    FlightResponseDTO getCheapestFlight();
    FlightResponseDTO getMostExpensiveFlight();
    long getFlightCount();
}
