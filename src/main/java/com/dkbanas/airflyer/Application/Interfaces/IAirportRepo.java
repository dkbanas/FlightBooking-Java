package com.dkbanas.airflyer.Application.Interfaces;

import com.dkbanas.airflyer.Domain.Entities.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAirportRepo extends JpaRepository<Airport, Long> {

    /**Retrieves all airports with pagination.*/
    Page<Airport> findAll(Pageable pageable);

    /**Finds an airport by its unique code.*/
    Optional<Airport> findAirportByCode(String code);

    /**Searches for airports based on a query string that matches name, city, country, or code.*/
    @Query("SELECT a FROM Airport a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(a.city) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(a.country) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(a.code) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Airport> searchAirportsByQuery(@Param("query") String query);

    /**Retrieves airports filtered by continent with pagination.*/
    @Query("SELECT a FROM Airport a WHERE (:continent IS NULL OR a.continent = :continent)")
    Page<Airport> findByContinent(
            @Param("continent") String continent,
            Pageable pageable
    );
}
