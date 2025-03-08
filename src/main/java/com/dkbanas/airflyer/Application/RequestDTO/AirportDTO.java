package com.dkbanas.airflyer.Application.RequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirportDTO {
    private String name;
    private String code;
    private String city;
    private String country;
    private String cityPhotoUrl;
    private String continent;
}
