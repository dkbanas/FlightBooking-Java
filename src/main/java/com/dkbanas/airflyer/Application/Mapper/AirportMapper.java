package com.dkbanas.airflyer.Application.Mapper;

import com.dkbanas.airflyer.Application.RequestDTO.AirportDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Airport | Request -> Entity, Entity -> Response
@Component
public class AirportMapper {
    // Converts an AirportDTO to an Airport entity
    public Airport toEntity(AirportDTO airportDTO) {
        Airport airport = new Airport();
        airport.setName(airportDTO.getName());
        airport.setCode(airportDTO.getCode());
        airport.setCity(airportDTO.getCity());
        airport.setCountry(airportDTO.getCountry());
        airport.setCreatedAt(LocalDateTime.now());
        airport.setCityPhotoUrl(airportDTO.getCityPhotoUrl());
        airport.setContinent(airportDTO.getContinent());
        return airport;
    }
    // Converts an Airport entity to an AirportResponseDTO
    public AirportResponseDTO toResponseDTO(Airport airport) {
        AirportResponseDTO responseDTO = new AirportResponseDTO();
        responseDTO.setId(airport.getId());
        responseDTO.setName(airport.getName());
        responseDTO.setCode(airport.getCode());
        responseDTO.setCity(airport.getCity());
        responseDTO.setCountry(airport.getCountry());
        responseDTO.setCreatedAt(airport.getCreatedAt());
        responseDTO.setCityPhotoUrl(airport.getCityPhotoUrl());
        responseDTO.setContinent(airport.getContinent());
        return responseDTO;
    }

    // Converts a list of Airport entities to a list of AirportResponseDTOs
    public List<AirportResponseDTO> toResponseDTOList(List<Airport> airports) {
        return airports.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Converts a paginated list of Airport objects into a paginated list of AirportResponseDTO objects
    public Page<AirportResponseDTO> toResponseDTOPage(Page<Airport> airportPage) {
        return airportPage.map(this::toResponseDTO);
    }
}
