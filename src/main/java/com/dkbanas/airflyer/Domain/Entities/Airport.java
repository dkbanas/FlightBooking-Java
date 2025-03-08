package com.dkbanas.airflyer.Domain.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "airports")
public class Airport {
    /** Unique identifier for the airport */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the airport */
    private String name;

    /** Unique airport code (e.g., IATA), required */
    @Column(unique = true,nullable = false)
    private String code;

    /** City where the airport is located */
    private String city;

    /** Country where the airport is located */
    private String country;

    /** Timestamp when the record was created */
    private LocalDateTime createdAt = LocalDateTime.now();

    /** URL to a city photo */
    private String cityPhotoUrl;

    /** Continent where the airport is located */
    private String continent;
}
