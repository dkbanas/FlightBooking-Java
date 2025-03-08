package com.dkbanas.airflyer.Application.Interfaces;

import com.dkbanas.airflyer.Application.RequestDTO.AirportDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AirportResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.Airport;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IAirportService {
    List<AirportResponseDTO> getAllAirports();
    Page<AirportResponseDTO> getAirportsPaginatedAndSorted(int page, int size, String sortBy, String sortDirection,String continent);
    List<AirportResponseDTO> searchAirports(String query);
    AirportResponseDTO getAirportByCode(String code);
    AirportResponseDTO createAirport(AirportDTO airport);
    AirportResponseDTO updateAirport(String code, AirportDTO airport);
    void deleteAirport(String code);
    long getAirportCount();
}
