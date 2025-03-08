package com.dkbanas.airflyer.Shared;


import com.dkbanas.airflyer.Application.RequestDTO.AirportDTO;
import com.dkbanas.airflyer.Application.RequestDTO.FlightDTO;
import com.dkbanas.airflyer.Application.RequestDTO.RegisterDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.FlightResponseDTO;
import com.dkbanas.airflyer.Infrastructure.AirportService;
import com.dkbanas.airflyer.Infrastructure.AuthService;
import com.dkbanas.airflyer.Infrastructure.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final AirportService airportService;
    private final AuthService authService;
    private final FlightService flightService;

    //Use only after deleting database
    @Override
    public void run(String... args) throws Exception {
//        seedUsers();
//        seedAirports();
//        seedFlights();
    }

    private void seedUsers() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load the JSON file with user data
            File file = new File("src/main/resources/users.json");
            List<RegisterDTO> users = objectMapper.readValue(file, new TypeReference<List<RegisterDTO>>() {});

            // Register each user
            for (RegisterDTO user : users) {
                try {
                    authService.register(user);
                    System.out.println("User registered: " + user.getEmail());
                } catch (Exception e) {
                    System.out.println("Skipping user: " + user.getEmail() + " (Error: " + e.getMessage() + ")");
                }
            }

            System.out.println("User database seeding completed successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        }
    }

    private void seedAirports() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load the JSON file with airport data
            File file = new File("src/main/resources/airports.json");
            List<AirportDTO> airports = objectMapper.readValue(file, new TypeReference<List<AirportDTO>>() {});

            // Save each airport if it doesn't already exist
            for (AirportDTO airportDTO : airports) {
                try {
                    airportService.createAirport(airportDTO);
                } catch (Exception e) {
                    System.out.println("Skipping duplicate or invalid airport: " + airportDTO.getCode());
                }
            }

            System.out.println("Database seeding completed successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        }
    }

    private void seedFlights() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load the JSON file with flight data
            File file = new File("src/main/resources/flights.json");
            List<FlightDTO> flightDTOs = objectMapper.readValue(file, new TypeReference<List<FlightDTO>>() {});

            // Create each flight
            for (FlightDTO flightDTO : flightDTOs) {
                try {
                    FlightResponseDTO createdFlight = flightService.createFlight(flightDTO);
                    System.out.println("Flight created: " + createdFlight.getFlightNumber());
                } catch (Exception e) {
                    System.out.println("Skipping flight: " + flightDTO.getFlightNumber() + " (Error: " + e.getMessage() + ")");
                }
            }

            System.out.println("Flight database seeding completed successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the JSON file: " + e.getMessage());
        }
    }
}
