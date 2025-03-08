package com.dkbanas.airflyer.Application.ResponseDTO;

import lombok.Getter;

import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private long expiresIn;
}
