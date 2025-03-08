package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.Interfaces.IAirportRepo;
import com.dkbanas.airflyer.Application.Mapper.AirportMapper;
import com.dkbanas.airflyer.Application.RequestDTO.AirportDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import com.dkbanas.airflyer.Shared.AppExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private IAirportRepo airportRepo;

    @Mock
    private AirportMapper airportMapper;

    @InjectMocks
    private AirportService airportService;

    private AirportDTO airportDTO;
    private Airport airport;
    private AirportResponseDTO airportResponseDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        airportDTO = new AirportDTO();
        airportDTO.setName("Test Airport");
        airportDTO.setCode("ABC");
        airportDTO.setCity("Test City");
        airportDTO.setCountry("Test Country");
        airportDTO.setCityPhotoUrl("http://example.com/photo.jpg");
        airportDTO.setContinent("Europe");

        airport = new Airport();
        airport.setId(1L);
        airport.setName("Test Airport");
        airport.setCode("ABC");
        airport.setCity("Test City");
        airport.setCountry("Test Country");
        airport.setCreatedAt(LocalDateTime.now());
        airport.setCityPhotoUrl("http://example.com/photo.jpg");
        airport.setContinent("Europe");

        airportResponseDTO = new AirportResponseDTO();
        airportResponseDTO.setId(1L);
        airportResponseDTO.setName("Test Airport");
        airportResponseDTO.setCode("ABC");
        airportResponseDTO.setCity("Test City");
        airportResponseDTO.setCountry("Test Country");
        airportResponseDTO.setCreatedAt(LocalDateTime.now());
        airportResponseDTO.setCityPhotoUrl("http://example.com/photo.jpg");
        airportResponseDTO.setContinent("Europe");
    }

    @Test
    void testGetAirportByCode() {
        // Arrange
        String code = "ABC";
        when(airportRepo.findAirportByCode(code)).thenReturn(Optional.of(airport));
        when(airportMapper.toResponseDTO(airport)).thenReturn(airportResponseDTO);

        // Act
        AirportResponseDTO result = airportService.getAirportByCode(code);

        // Assert
        assertNotNull(result);
        assertEquals(code, result.getCode());
        verify(airportRepo, times(1)).findAirportByCode(code);
        verify(airportMapper, times(1)).toResponseDTO(airport);
    }

    @Test
    void testGetAirportByCodeNotFound() {
        // Arrange
        String code = "INVALID";
        when(airportRepo.findAirportByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppExceptions.AirportNotFoundException.class, () -> airportService.getAirportByCode(code));
    }

    @Test
    void testCreateAirport() {
        // Arrange
        when(airportRepo.findAirportByCode(airportDTO.getCode())).thenReturn(Optional.empty());
        when(airportMapper.toEntity(airportDTO)).thenReturn(airport);
        when(airportRepo.save(airport)).thenReturn(airport);
        when(airportMapper.toResponseDTO(airport)).thenReturn(airportResponseDTO);

        // Act
        AirportResponseDTO result = airportService.createAirport(airportDTO);

        // Assert
        assertNotNull(result);
        assertEquals(airportDTO.getCode(), result.getCode());
        verify(airportRepo, times(1)).findAirportByCode(airportDTO.getCode());
        verify(airportRepo, times(1)).save(airport);
    }

    @Test
    void testCreateAirportAlreadyExists() {
        // Arrange
        when(airportRepo.findAirportByCode(airportDTO.getCode())).thenReturn(Optional.of(airport));

        // Act & Assert
        assertThrows(AppExceptions.AirportAlreadyExistsException.class, () -> airportService.createAirport(airportDTO));
    }

    @Test
    void testUpdateAirport() {
        // Arrange
        String code = "ABC";
        when(airportRepo.findAirportByCode(code)).thenReturn(Optional.of(airport));
        when(airportRepo.save(airport)).thenReturn(airport);
        when(airportMapper.toResponseDTO(airport)).thenReturn(airportResponseDTO);

        // Act
        AirportResponseDTO result = airportService.updateAirport(code, airportDTO);

        // Assert
        assertNotNull(result);
        assertEquals(airportDTO.getName(), result.getName());
        verify(airportRepo, times(1)).findAirportByCode(code);
        verify(airportRepo, times(1)).save(airport);
    }

    @Test
    void testDeleteAirport() {
        // Arrange
        String code = "ABC";
        when(airportRepo.findAirportByCode(code)).thenReturn(Optional.of(airport));

        // Act
        airportService.deleteAirport(code);

        // Assert
        verify(airportRepo, times(1)).findAirportByCode(code);
        verify(airportRepo, times(1)).delete(airport);
    }
}