package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.Interfaces.IFlightRepo;
import com.dkbanas.airflyer.Application.Mapper.AirportMapper;
import com.dkbanas.airflyer.Application.RequestDTO.AirportDTO;
import com.dkbanas.airflyer.Application.Interfaces.IAirportRepo;
import com.dkbanas.airflyer.Application.Interfaces.IAirportService;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportFlightCountDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import com.dkbanas.airflyer.Shared.AppExceptions;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AirportService implements IAirportService {

    private static final String IMAGE_DIR = "src/main/resources/static/images/airports/";
    private final IAirportRepo airportRepo;
    private final AirportMapper airportMapper;
    private final IFlightRepo flightRepo;

    /*Retrieves all airports from the database.*/
    @Override
    public List<AirportResponseDTO> getAllAirports() {
        List<Airport> airports = airportRepo.findAll();
        return airportMapper.toResponseDTOList(airports);
    }

    /*Retrieves all airports paginated and sorted from the database. */
    @Override
    public Page<AirportResponseDTO> getAirportsPaginatedAndSorted(int page, int size, String sortBy, String sortDirection, String continent) {
        Pageable pageable = PageRequest.of(page, size,
                sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        String continentToUse = (continent == null || continent.isEmpty()) ? null : continent;
        Page<Airport> airportPage = airportRepo.findByContinent(continentToUse, pageable);

        return airportMapper.toResponseDTOPage(airportPage);
    }

    /*Searches for airports based on a query string in their name, city, country, or code.*/
    @Override
    public List<AirportResponseDTO> searchAirports(String query) {
        List<Airport> airports = airportRepo.searchAirportsByQuery(query);
        return airportMapper.toResponseDTOList(airports);
    }

    /*Retrieves an airport by its code.*/
    @Override
    public AirportResponseDTO getAirportByCode(String code) {
        Airport airport = airportRepo.findAirportByCode(code)
                .orElseThrow(() -> new AppExceptions.AirportNotFoundException("Airport with code " + code + " not found"));
        return airportMapper.toResponseDTO(airport);
    }

    /*Creates a new airport based on the provided details.*/
    @Override
    public AirportResponseDTO createAirport(AirportDTO request) {
        Optional<Airport> existingAirport = airportRepo.findAirportByCode(request.getCode());
        if (existingAirport.isPresent()) {
            throw new AppExceptions.AirportAlreadyExistsException("Airport with code " + request.getCode() + " already exists");
        }
        Airport airport = airportMapper.toEntity(request);
        Airport savedAirport = airportRepo.save(airport);
        return airportMapper.toResponseDTO(savedAirport);
    }

    /*Updates an existing airport's details.*/
    @Override
    public AirportResponseDTO updateAirport(String code, AirportDTO request) {
        Airport existingAirport = airportRepo.findAirportByCode(code)
                .orElseThrow(() -> new AppExceptions.AirportNotFoundException("Airport with code " + code + " not found"));

        existingAirport.setName(request.getName());
        existingAirport.setCity(request.getCity());
        existingAirport.setCountry(request.getCountry());
        Airport updatedAirport = airportRepo.save(existingAirport);
        return airportMapper.toResponseDTO(updatedAirport);
    }

    /*Deletes an airport by its code.*/
    @Override
    public void deleteAirport(String code) {
        Airport existingAirport = airportRepo.findAirportByCode(code)
                .orElseThrow(() -> new AppExceptions.AirportNotFoundException("Airport with code " + code + " not found"));
        airportRepo.delete(existingAirport);
    }

    /*Uploads an image for an airport.*/
    public String uploadAirportImage(MultipartFile file, String airportCode) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        try {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String newFileName = airportCode + "." + fileExtension;

            Path targetLocation = Paths.get(IMAGE_DIR).resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/images/airports/" + newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    /*Gets the file extension from the provided file name.*/
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /*Retrieves busiest airport sorted by number of departures and arrivals */
    public Optional<AirportFlightCountDTO> getBusiestAirport() {
        Map<Long, AirportFlightCountDTO> airportCountMap = new HashMap<>();

        // Process departures
        for (Object[] row : flightRepo.countFlightsByDeparture()) {
            Airport airport = (Airport) row[0];
            Long count = (Long) row[1];

            airportCountMap.putIfAbsent(airport.getId(), new AirportFlightCountDTO(airportMapper.toResponseDTO(airport), 0L));
            airportCountMap.get(airport.getId()).setFlightCount(airportCountMap.get(airport.getId()).getFlightCount() + count);
        }

        // Process arrivals
        for (Object[] row : flightRepo.countFlightsByArrival()) {
            Airport airport = (Airport) row[0];
            Long count = (Long) row[1];

            airportCountMap.putIfAbsent(airport.getId(), new AirportFlightCountDTO(airportMapper.toResponseDTO(airport), 0L));
            airportCountMap.get(airport.getId()).setFlightCount(airportCountMap.get(airport.getId()).getFlightCount() + count);
        }

        // Find the airport with the highest flight count
        return airportCountMap.values().stream()
                .max(Comparator.comparingLong(AirportFlightCountDTO::getFlightCount));
    }

    /*Retrieve a number of account */
    @Override
    public long getAirportCount() {
        return airportRepo.count();
    }
}
