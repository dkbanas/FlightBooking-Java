package com.dkbanas.airflyer.Application.Interfaces;

import com.dkbanas.airflyer.Application.ResponseDTO.ReservationResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IReservationService {
    ReservationResponseDTO createReservation(Long flightId, Integer userId, Set<String> seatNumbers);
    void cancelReservation(Long reservationId);
    List<ReservationResponseDTO> getUserReservations(Integer userId);
    BigDecimal calculateTotalProfit();
    long getReservationsCount();
}
