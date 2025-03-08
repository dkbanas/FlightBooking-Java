package com.dkbanas.airflyer.Infrastructure;

import com.dkbanas.airflyer.Application.RequestDTO.LoginDTO;
import com.dkbanas.airflyer.Application.RequestDTO.RegisterDTO;
import com.dkbanas.airflyer.Application.Interfaces.IUserRepo;
import com.dkbanas.airflyer.Application.ResponseDTO.AccountDetailsResponseDTO;
import com.dkbanas.airflyer.Domain.Entities.RegisteredUser;
import com.dkbanas.airflyer.Shared.AppExceptions;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {

    private final IUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /*Registers a new user account */
    public void register(RegisterDTO input) {
        if (userRepo.findByEmail(input.getEmail()).isPresent()) {
            throw new AppExceptions.AccountAlreadyExistsException("Account with this email already exists.");
        }
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setEmail(input.getEmail());
        registeredUser.setPassword(passwordEncoder.encode(input.getPassword()));
        registeredUser.setAuthorities(List.of("ROLE_USER"));

        userRepo.save(registeredUser);
    }

    /*Authenticates a user*/
    public void login(LoginDTO input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AppExceptions.InvalidCredentialsException("Invalid username or password.");
        }
    }

    /*Retrieve account details */
    public AccountDetailsResponseDTO getAccountDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        RegisteredUser user = userRepo.findByEmail(userEmail).orElseThrow(() -> new AppExceptions.UserNotFoundException("User not found."));

        AccountDetailsResponseDTO accountDetailsResponseDTO = new AccountDetailsResponseDTO();
        accountDetailsResponseDTO.setUserId(user.getId());
        accountDetailsResponseDTO.setEmail(user.getEmail());
        accountDetailsResponseDTO.setRoles(user.getAuthorities());
        return accountDetailsResponseDTO;
    }

    /*Retrieve a number of account */
    public long getUserCount() {
        return userRepo.count();
    }
}
