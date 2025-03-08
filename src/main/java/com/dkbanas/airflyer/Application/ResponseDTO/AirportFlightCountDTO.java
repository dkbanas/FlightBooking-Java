package com.dkbanas.airflyer.Application.ResponseDTO;

public class AirportFlightCountDTO {
    private AirportResponseDTO airport;
    private Long flightCount;

    // Constructor, getters, and setters
    public AirportFlightCountDTO(AirportResponseDTO airport, Long flightCount) {
        this.airport = airport;
        this.flightCount = flightCount;
    }

    public AirportResponseDTO getAirport() {
        return airport;
    }

    public void setAirport(AirportResponseDTO airport) {
        this.airport = airport;
    }

    public Long getFlightCount() {
        return flightCount;
    }

    public void setFlightCount(Long flightCount) {
        this.flightCount = flightCount;
    }
}
