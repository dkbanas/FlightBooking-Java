package com.dkbanas.airflyer.Application.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class AirportResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String city;
    private String country;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private String cityPhotoUrl;
    private String continent;
}