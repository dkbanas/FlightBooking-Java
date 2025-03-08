package com.dkbanas.airflyer.Application.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private AirportResponseDTO departureAirport;
    private AirportResponseDTO arrivalAirport;
    private Long reservationCount;
}
