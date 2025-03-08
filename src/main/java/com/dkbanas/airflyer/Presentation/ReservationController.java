package com.dkbanas.airflyer.Presentation;

import com.dkbanas.airflyer.Application.ResponseDTO.ReservationResponseDTO;
import com.dkbanas.airflyer.Application.ResponseDTO.RouteDTO;
import com.dkbanas.airflyer.Domain.Entities.Reservation;
import com.dkbanas.airflyer.Infrastructure.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Create a reservation")
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @RequestParam Long flightId,
            @RequestParam Integer userId,
            @RequestParam Set<String> seatNumbers) {
        ReservationResponseDTO reservation = reservationService.createReservation(flightId, userId, seatNumbers);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reservations for a user")
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations(@PathVariable Integer userId) {
        List<ReservationResponseDTO> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/{reservationId}")
    @Operation(summary = "Cancel a reservation")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-profit")
    public ResponseEntity<BigDecimal> getTotalProfit() {
        BigDecimal totalProfit = reservationService.calculateTotalProfit();
        return ResponseEntity.ok(totalProfit);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getReservationsCount() {
        long reservationsCount = reservationService.getReservationsCount();
        return ResponseEntity.ok(reservationsCount);
    }

    @GetMapping("/most-popular-route")
    public ResponseEntity<RouteDTO> getMostPopularRoute() {
        return reservationService.getMostPopularRoute()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
