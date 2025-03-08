package com.dkbanas.airflyer.Application.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FlightResponseDTO {
    private Long id;
    private String flightNumber;
    private AirportResponseDTO departureLocation;
    private AirportResponseDTO arrivalLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalTime;
    private BigDecimal priceEconomy;
    private int totalSeatsEconomy;
    private String duration;
    private String airline;
    private int availableSeatsEconomy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private List<String> availableSeatsEconomyList;
}
