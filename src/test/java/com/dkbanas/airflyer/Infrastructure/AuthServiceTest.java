package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.Interfaces.IUserRepo;
import com.dkbanas.airflyer.Application.RequestDTO.LoginDTO;
import com.dkbanas.airflyer.Application.RequestDTO.RegisterDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.AccountDetailsResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.RegisteredUser;
import com.dkbanas.airflyer.Shared.AppExceptions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private IUserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegister_Success() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setPassword("password");

        when(userRepo.findByEmail(registerDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPassword");

        // Act
        authService.register(registerDTO);

        // Assert
        verify(userRepo, times(1)).findByEmail(registerDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(registerDTO.getPassword());
        verify(userRepo, times(1)).save(any(RegisteredUser.class));
    }

    @Test
    public void testRegister_AccountAlreadyExists() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setPassword("password");

        when(userRepo.findByEmail(registerDTO.getEmail())).thenReturn(Optional.of(new RegisteredUser()));

        // Act & Assert
        assertThrows(AppExceptions.AccountAlreadyExistsException.class, () -> {
            authService.register(registerDTO);
        });

        verify(userRepo, times(1)).findByEmail(registerDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepo, never()).save(any(RegisteredUser.class));
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        // Act
        authService.login(loginDTO);

        // Assert
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testLogin_InvalidCredentials() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(AppExceptions.InvalidCredentialsException.class, () -> {
            authService.login(loginDTO);
        });

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testGetAccountDetails_Success() {
        // Arrange
        RegisteredUser user = new RegisteredUser();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setAuthorities(List.of("ROLE_USER"));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        AccountDetailsResponseDTO response = authService.getAccountDetails();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getUserId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(List.of("ROLE_USER"), response.getRoles());

        verify(userRepo, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testGetAccountDetails_UserNotFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppExceptions.UserNotFoundException.class, () -> {
            authService.getAccountDetails();
        });

        verify(userRepo, times(1)).findByEmail("test@example.com");
    }


}