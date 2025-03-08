package com.dkbanas.airflyer.Presentation;


import com.dkbanas.airflyer.Application.RequestDTO.LoginDTO;
import com.dkbanas.airflyer.Application.RequestDTO.RegisterDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AccountDetailsResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.LoginResponseDTO;
import com.dkbanas.airflyer.Infrastructure.AuthService;
import com.dkbanas.airflyer.Shared.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "The Authentication API. Contains login and register operations.")
public class AuthController {
    private final JwtService jwtService;
    private final AuthService authenticationService;

    @Operation(summary = "Create new Account")
    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> register(@RequestBody RegisterDTO registerDTO) {
            authenticationService.register(registerDTO);
            return ResponseEntity.ok(registerDTO);
    }

    @Operation(summary = "Login to your Account")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
            authenticationService.login(loginDTO);
            String jwtToken = jwtService.generateToken(loginDTO.getEmail());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setToken(jwtToken);
            loginResponseDTO.setExpiresIn(jwtService.getExpirationTime());
            return ResponseEntity.ok(loginResponseDTO);
    }

    @Operation(summary = "Get account details")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/account-details")
    public ResponseEntity<AccountDetailsResponseDTO> getAccountDetails() {
        AccountDetailsResponseDTO accountDetails = authenticationService.getAccountDetails();
        return ResponseEntity.ok(accountDetails);
    }

    @GetMapping("/user-count")
    public ResponseEntity<Long> getUserCount() {
        long userCount = authenticationService.getUserCount();
        return ResponseEntity.ok(userCount);
    }
}
