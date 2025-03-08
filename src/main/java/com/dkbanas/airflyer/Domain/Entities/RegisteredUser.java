package com.dkbanas.airflyer.Domain.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class RegisteredUser {

    /** Unique identifier for the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Unique email address of the user */
    @Column(unique = true)
    private String email;

    /** Password of the user (encrypted) */
    @Column(nullable = false)
    private String password;

    /** List of authorities (roles) assigned to the user */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;

    /**
     * Converts user details into a Spring Security UserDetails object.
     * @return UserDetails object
     */
    public UserDetails toUserDetails() {
        return new User(
                getEmail(),
                getPassword(),
                authorities == null ? new ArrayList<>() : authorities
                        .stream()
                        .map(a -> new SimpleGrantedAuthority(a))
                        .collect(Collectors.toList())
        );
    }
}
