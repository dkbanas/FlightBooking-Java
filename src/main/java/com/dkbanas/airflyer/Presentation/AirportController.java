package com.dkbanas.airflyer.Presentation;

import com.dkbanas.airflyer.Application.RequestDTO.AirportDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportFlightCountDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Infrastructure.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/airport")
@RestController
@RequiredArgsConstructor
@Tag(name = "Airport", description = "The Airport API. Contains all the operations that can be performed on an Airport.")
public class AirportController {

    private final AirportService airportService;

    @Operation(summary = "Get All Airports")
    @GetMapping
    public ResponseEntity<List<AirportResponseDTO>> getAllAirports() {
        List<AirportResponseDTO> airports = airportService.getAllAirports();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(airports);
    }

    @Operation(summary = "Get paginated, sorted and filtered list of Airports")
    @GetMapping("/paginated")
    public ResponseEntity<Page<AirportResponseDTO>> getAirportsPaginatedAndSorted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String continent
    ) {
        Page<AirportResponseDTO> airportsPage = airportService.getAirportsPaginatedAndSorted(page, size, sortBy, sortDirection,continent);
        return ResponseEntity.ok(airportsPage);
    }

    @Operation(summary = "Search Airports by Name, City, Country, or Code")
    @GetMapping("/search")
    public ResponseEntity<List<AirportResponseDTO>> searchAirports(@RequestParam("query") String query) {
        List<AirportResponseDTO> airports = airportService.searchAirports(query);
        return ResponseEntity.ok(airports);
    }

    @Operation(summary = "Get Airport By Code")
    @GetMapping("/{code}")
    public ResponseEntity<AirportResponseDTO> getAirportByCode(@PathVariable String code) {
        AirportResponseDTO airport = airportService.getAirportByCode(code);
        return ResponseEntity.ok(airport);
    }

    @Operation(summary = "Create new Airport")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<AirportResponseDTO> createAirport(@RequestBody AirportDTO airportDTO) {
        AirportResponseDTO createdAirport = airportService.createAirport(airportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAirport);
    }

    @Operation(summary = "Update existing Airport")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{code}")
    public ResponseEntity<AirportResponseDTO> updateAirport(@PathVariable String code, @RequestBody AirportDTO airportDTO) {
        AirportResponseDTO updatedAirport = airportService.updateAirport(code, airportDTO);
        return ResponseEntity.ok(updatedAirport);
    }

    @Operation(summary = "Delete existing Airport")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{code}")
    public void deleteAirport(@PathVariable String code) {
        airportService.deleteAirport(code);
    }

    @Operation(summary = "Upload Airport Image")
    @PostMapping("/{code}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable String code, @RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = airportService.uploadAirportImage(file, code);

            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/busiest")
    public ResponseEntity<AirportFlightCountDTO> getBusiestAirport() {
        return airportService.getBusiestAirport()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/airport-count")
    public ResponseEntity<Long> getAirportCount() {
        long airportCount = airportService.getAirportCount();
        return ResponseEntity.ok(airportCount);
    }
}
