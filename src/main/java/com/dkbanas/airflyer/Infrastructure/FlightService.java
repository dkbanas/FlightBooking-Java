package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.Interfaces.IAirportRepo;
import com.dkbanas.airflyer.Application.Interfaces.IFlightRepo;
import com.dkbanas.airflyer.Application.Interfaces.IFlightService;
import com.dkbanas.airflyer.Application.Mapper.FlightMapper;
import com.dkbanas.airflyer.Application.RequestDTO.FlightDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.FlightResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.RoundTripFlightResponseDTO;
import com.dkbanas.airflyer.Application.Utils.DurationCalculator;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import com.dkbanas.airflyer.Domain.Entities.Flight;
import com.dkbanas.airflyer.Shared.AppExceptions;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class FlightService implements IFlightService {

    private final IFlightRepo flightRepo;
    private final IAirportRepo airportRepo;
    private final FlightMapper flightMapper;

    /*Retrieves all flights from the database. */
    @Override
    public List<FlightResponseDTO> getAllFlights() {
        return flightRepo.findAll().stream()
                .map(this::mapFlightToResponseDTO)
                .toList();
    }

    /*Retrieves all flights paginated and sorted from the database. */
    @Override
    public Page<FlightResponseDTO> getFlightsPaginatedAndSorted(int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        return flightMapper.toResponseDTOPage(flightRepo.findAll(pageable));
    }

    /*Retrieves a flight by its flight number. */
    @Override
    public FlightResponseDTO getFlightByFlightNumber(String flightNumber) {
        Flight flight = flightRepo.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new AppExceptions.FlightNotFoundException("Flight with number " + flightNumber + " not found"));
        return mapFlightToResponseDTO(flight);
    }

    /*Creates a new flight based on the provided details. */
    @Override
    public FlightResponseDTO createFlight(FlightDTO flightDTO) {
        Airport departureLocation = airportRepo.findById(flightDTO.getDepartureLocationId())
                .orElseThrow(() -> new AppExceptions.AirportNotFoundException("Invalid departure airport ID"));
        Airport arrivalLocation = airportRepo.findById(flightDTO.getArrivalLocationId())
                .orElseThrow(() -> new AppExceptions.AirportNotFoundException("Invalid arrival airport ID"));

        Flight flight = flightMapper.toEntity(flightDTO, departureLocation, arrivalLocation);
        flight.setDuration(DurationCalculator.calculateDuration(flight.getDepartureTime(), flight.getArrivalTime()));

        flight = flightRepo.save(flight);
        return flightMapper.toResponseDTO(flight);
    }

    /*Deletes a flight by its flight number. */
    @Override
    public void deleteFlight(String flightNumber) {
        Flight existingFlight = flightRepo.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new AppExceptions.FlightNotFoundException("Flight with number " + flightNumber + " not found"));
        flightRepo.delete(existingFlight);
    }

    /*Searches for flights based on departure and arrival locations, dates, and passenger count. */
    @Override
    public List<RoundTripFlightResponseDTO> searchFlights(
            Long departureLocationId, Long arrivalLocationId,
            LocalDate departureDate, LocalDate returnDate,
            int numberOfPassengers, boolean roundTrip
    ) {
        LocalDateTime departureStart = departureDate.atStartOfDay();
        LocalDateTime departureEnd = departureDate.atTime(LocalTime.MAX);

        List<FlightResponseDTO> outboundDTOs = findOutboundFlights(
                departureLocationId, arrivalLocationId, departureStart, departureEnd, numberOfPassengers
        );

        if (!roundTrip) {
            return outboundDTOs.stream()
                    .map(dto -> new RoundTripFlightResponseDTO(dto, null))
                    .toList();
        }

        LocalDateTime returnStart = returnDate.atStartOfDay();
        LocalDateTime returnEnd = returnDate.atTime(LocalTime.MAX);

        List<FlightResponseDTO> returnDTOs = findReturnFlights(
                arrivalLocationId, departureLocationId, returnStart, returnEnd, numberOfPassengers
        );

        return outboundDTOs.stream()
                .flatMap(outbound -> returnDTOs.stream()
                        .map(returnFlight -> new RoundTripFlightResponseDTO(outbound, returnFlight)))
                .toList();
    }

    /*Retrieve cheapest flight */
    public FlightResponseDTO getCheapestFlight() {
        return flightMapper.toResponseDTO(flightRepo.findTopByOrderByPriceEconomyAsc());
    }

    /*Retrieve most expensive flight */
    public FlightResponseDTO getMostExpensiveFlight() {
        return flightMapper.toResponseDTO(flightRepo.findTopByOrderByPriceEconomyDesc());
    }

    /*Maps a flight entity to a DTO */
    private FlightResponseDTO mapFlightToResponseDTO(Flight flight) {
        FlightResponseDTO dto = flightMapper.toResponseDTO(flight);
        dto.setAvailableSeatsEconomyList(getAvailableSeats(
                flight.getTotalSeatsEconomy(), flight.getOccupiedSeatsEconomy()
        ));
        return dto;
    }

    /*Retrieves a list of available seats for a flight. */
    private List<String> getAvailableSeats(int totalSeats, Set<String> occupiedSeats) {
        return IntStream.rangeClosed(1, totalSeats)
                .mapToObj(String::valueOf)
                .filter(seat -> !occupiedSeats.contains(seat))
                .toList();
    }

    /* Creates a Pageable object for pagination and sorting. */
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        return PageRequest.of(page, size,
                sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
    }

    /* Retrieves a list of outbound flights based on search criteria. */
    private List<FlightResponseDTO> findOutboundFlights(Long departureLocationId, Long arrivalLocationId,
                                                        LocalDateTime departureStart, LocalDateTime departureEnd,
                                                        int numberOfPassengers) {
        List<Flight> outboundFlights = flightRepo.findAvailableFlights(
                departureLocationId, arrivalLocationId, departureStart, departureEnd, numberOfPassengers
        );
        return outboundFlights.stream()
                .map(this::mapFlightToResponseDTO)
                .toList();
    }

    /* Retrieves a list of return flights based on search criteria. */
    private List<FlightResponseDTO> findReturnFlights(Long arrivalLocationId, Long departureLocationId,
                                                      LocalDateTime returnStart, LocalDateTime returnEnd,
                                                      int numberOfPassengers) {
        List<Flight> returnFlights = flightRepo.findAvailableFlights(
                arrivalLocationId, departureLocationId, returnStart, returnEnd, numberOfPassengers
        );
        return returnFlights.stream()
                .map(this::mapFlightToResponseDTO)
                .toList();
    }

    /*Retrieve a number of flights */
    @Override
    public long getFlightCount() {
        return flightRepo.count();
    }
}
