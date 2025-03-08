package com.dkbanas.airflyer.Shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Not found
    @ExceptionHandler(AppExceptions.UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(AppExceptions.UserNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AppExceptions.AirportNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAirportNotFoundException(AppExceptions.AirportNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AppExceptions.FlightNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleFlightNotFoundException(AppExceptions.FlightNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AppExceptions.ReservationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReservationNotFoundException(AppExceptions.ReservationNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //Already Exist / Already Taken

    @ExceptionHandler(AppExceptions.AccountAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAccountAlreadyExistsException(AppExceptions.AccountAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AppExceptions.AirportAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAirportAlreadyExistsException(AppExceptions.AirportAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AppExceptions.FlightAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleFlightAlreadyExistsException(AppExceptions.FlightAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AppExceptions.SeatAlreadyTakenException.class)
    public ResponseEntity<Map<String, String>> handleSeatAlreadyTakenException(AppExceptions.SeatAlreadyTakenException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //Invalid

    @ExceptionHandler(AppExceptions.InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(AppExceptions.InvalidCredentialsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }




}
