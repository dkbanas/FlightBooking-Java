package com.dkbanas.airflyer.Shared;

public class AppExceptions {

    //Not Found
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class AirportNotFoundException extends RuntimeException {
        public AirportNotFoundException(String message) {
            super(message);
        }
    }

    public static class FlightNotFoundException extends RuntimeException {
        public FlightNotFoundException(String message) {
            super(message);
        }
    }

    public static class ReservationNotFoundException extends RuntimeException {
        public ReservationNotFoundException(String message) {
            super(message);
        }
    }
    //Already Exist / Already taken
    public static class AccountAlreadyExistsException extends RuntimeException {
        public AccountAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class AirportAlreadyExistsException extends RuntimeException {
        public AirportAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class FlightAlreadyExistsException extends RuntimeException {
        public FlightAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class SeatAlreadyTakenException extends RuntimeException {
        public SeatAlreadyTakenException(String message) {
            super(message);
        }
    }

    //Invalid
    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }



}
