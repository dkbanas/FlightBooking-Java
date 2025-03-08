package com.dkbanas.airflyer.Presentation;
import com.dkbanas.airflyer.Application.RequestDTO.FlightDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.FlightResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.RoundTripFlightResponseDTO;
import com.dkbanas.airflyer.Infrastructure.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/flight")
@RestController
@RequiredArgsConstructor
@Tag(name = "Flight", description = "The Flight API. Contains all the operations that can be performed on the Flight.")
public class FlightController {
    private final FlightService flightService;

    @Operation(summary = "Get All Flights")
    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        List<FlightResponseDTO> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @Operation(summary = "Get paginated and sorted list of Flights")
    @GetMapping("/paginated")
    public ResponseEntity<Page<FlightResponseDTO>> getFlightsPaginatedAndSorted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "flightNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        Page<FlightResponseDTO> flightsPage = flightService.getFlightsPaginatedAndSorted(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(flightsPage);
    }

    @Operation(summary = "Get Flight By Flight Number")
    @GetMapping("/{flightNumber}")
    public ResponseEntity<FlightResponseDTO> getFlightByFlightNumber(@PathVariable String flightNumber) {
        FlightResponseDTO flight = flightService.getFlightByFlightNumber(flightNumber);
        return ResponseEntity.ok(flight);
    }


    @Operation(summary = "Create new Flight")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<FlightResponseDTO> createFlight(@RequestBody FlightDTO flightDTO) {
        FlightResponseDTO createdFlight = flightService.createFlight(flightDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }

    @Operation(summary = "Delete Flight")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{flightNumber}")
    public void deleteFlight(@PathVariable String flightNumber) {
        flightService.deleteFlight(flightNumber);
    }


    @Operation(summary = "Search Flights (One-Way or Round-Trip)")
    @GetMapping("/search")
    public ResponseEntity<List<RoundTripFlightResponseDTO>> searchFlights(
            @RequestParam Long departureLocationId,
            @RequestParam Long arrivalLocationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "1") int numberOfPassengers,
            @RequestParam(defaultValue = "false") boolean roundTrip
    ) {
        List<RoundTripFlightResponseDTO> flights = flightService.searchFlights(
                departureLocationId, arrivalLocationId, departureDate, returnDate, numberOfPassengers, roundTrip
        );
        return ResponseEntity.ok(flights);
    }

    @Operation(summary = "Get the cheapest flight")
    @GetMapping("/cheapest")
    public ResponseEntity<FlightResponseDTO> getCheapestFlight() {
        return ResponseEntity.ok(flightService.getCheapestFlight());
    }

    @Operation(summary = "Get the most expensive flight")
    @GetMapping("/most-expensive")
    public ResponseEntity<FlightResponseDTO> getMostExpensiveFlight() {
        return ResponseEntity.ok(flightService.getMostExpensiveFlight());
    }

    @GetMapping("/flight-count")
    public ResponseEntity<Long> getAirportCount() {
        long flightCount = flightService.getFlightCount();
        return ResponseEntity.ok(flightCount);
    }
}
