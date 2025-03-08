package com.dkbanas.airflyer.Application.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoundTripFlightResponseDTO {
    private FlightResponseDTO outboundFlight;
    private FlightResponseDTO returnFlight;
}