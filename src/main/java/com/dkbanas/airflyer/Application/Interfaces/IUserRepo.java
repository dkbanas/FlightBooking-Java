package com.dkbanas.airflyer.Application.Interfaces;

import com.dkbanas.airflyer.Domain.Entities.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepo extends JpaRepository<RegisteredUser, Integer> {
    /**Finds a user by email.*/
    Optional<RegisteredUser> findByEmail(String email);
}
