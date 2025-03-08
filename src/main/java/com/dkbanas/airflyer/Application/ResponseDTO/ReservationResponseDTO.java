package com.dkbanas.airflyer.Application.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ReservationResponseDTO {
    private Long id;
    private String flightNumber;
    private String userEmail;
    private Set<String> seatNumbers;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationDate;

    private String departureName;
    private String departureCity;
    private String departureCountry;
    private String arrivalName;
    private String arrivalCity;
    private String arrivalCountry;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;
    private BigDecimal priceEconomy;
}
