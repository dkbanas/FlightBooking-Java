package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.Interfaces.IAirportRepo;
import com.dkbanas.airflyer.Application.Interfaces.IFlightRepo;
import com.dkbanas.airflyer.Application.Mapper.FlightMapper;
import com.dkbanas.airflyer.Application.RequestDTO.FlightDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.FlightResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import com.dkbanas.airflyer.Domain.Entities.Flight;
import com.dkbanas.airflyer.Shared.AppExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    @Mock
    private IFlightRepo flightRepo;
    @Mock
    private IAirportRepo airportRepo;
    @Mock
    private FlightMapper flightMapper;
    @InjectMocks
    private FlightService flightService;

    private Flight flight;
    private FlightResponseDTO flightResponseDTO;
    private FlightDTO flightDTO;
    private Airport departureAirport;
    private Airport arrivalAirport;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setDepartureTime(LocalDateTime.of(2025, 2, 10, 10, 30));
        flight.setArrivalTime(LocalDateTime.of(2025, 2, 10, 12, 30));
        flight.setTotalSeatsEconomy(100);
        flight.setOccupiedSeatsEconomy(new HashSet<>(Arrays.asList("1", "2", "3")));
        flight.setPriceEconomy(BigDecimal.valueOf(150.00));

        flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.setFlightNumber("FL123");

        flightDTO = new FlightDTO();
        flightDTO.setDepartureLocationId(1L);
        flightDTO.setArrivalLocationId(2L);
        flightDTO.setDepartureTime(LocalDateTime.of(2025, 2, 10, 10, 30));
        flightDTO.setArrivalTime(LocalDateTime.of(2025, 2, 10, 12, 30));

        departureAirport = new Airport();
        departureAirport.setId(1L);

        arrivalAirport = new Airport();
        arrivalAirport.setId(2L);
    }

    @Test
    void testGetAllFlights() {
        when(flightRepo.findAll()).thenReturn(List.of(flight));
        when(flightMapper.toResponseDTO(any())).thenReturn(flightResponseDTO);

        List<FlightResponseDTO> flights = flightService.getAllFlights();

        assertEquals(1, flights.size());
        assertEquals("FL123", flights.get(0).getFlightNumber());
        verify(flightRepo, times(1)).findAll();
    }

    @Test
    void testGetFlightByFlightNumber_Found() {
        when(flightRepo.findByFlightNumber("FL123")).thenReturn(Optional.of(flight));
        when(flightMapper.toResponseDTO(any())).thenReturn(flightResponseDTO);

        FlightResponseDTO result = flightService.getFlightByFlightNumber("FL123");

        assertNotNull(result);
        assertEquals("FL123", result.getFlightNumber());
        verify(flightRepo, times(1)).findByFlightNumber("FL123");
    }

    @Test
    void testGetFlightByFlightNumber_NotFound() {
        when(flightRepo.findByFlightNumber("FL999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppExceptions.FlightNotFoundException.class, () -> {
            flightService.getFlightByFlightNumber("FL999");
        });

        assertEquals("Flight with number FL999 not found", exception.getMessage());
    }

    @Test
    void testCreateFlight() {
        when(airportRepo.findById(1L)).thenReturn(Optional.of(departureAirport));
        when(airportRepo.findById(2L)).thenReturn(Optional.of(arrivalAirport));
        when(flightMapper.toEntity(flightDTO, departureAirport, arrivalAirport)).thenReturn(flight);
        when(flightRepo.save(flight)).thenReturn(flight);
        when(flightMapper.toResponseDTO(flight)).thenReturn(flightResponseDTO);

        FlightResponseDTO result = flightService.createFlight(flightDTO);

        assertNotNull(result);
        assertEquals("FL123", result.getFlightNumber());
        verify(flightRepo, times(1)).save(any());
    }

    @Test
    void testDeleteFlight_Found() {
        when(flightRepo.findByFlightNumber("FL123")).thenReturn(Optional.of(flight));

        flightService.deleteFlight("FL123");

        verify(flightRepo, times(1)).delete(flight);
    }

    @Test
    void testDeleteFlight_NotFound() {
        when(flightRepo.findByFlightNumber("FL999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(AppExceptions.FlightNotFoundException.class, () -> {
            flightService.deleteFlight("FL999");
        });

        assertEquals("Flight with number FL999 not found", exception.getMessage());
        verify(flightRepo, never()).delete(any());
    }



}