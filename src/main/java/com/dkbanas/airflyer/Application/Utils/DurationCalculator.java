package com.dkbanas.airflyer.Application.Utils;

import java.time.Duration;
import java.time.LocalDateTime;

// Calculates the duration between the specified departure and arrival times.
public class DurationCalculator {
    public static String calculateDuration(LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (departureTime != null && arrivalTime != null) {
            Duration duration = Duration.between(departureTime, arrivalTime);
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            return String.format("%d h %d min", hours, minutes);
        }
        return "Duration not available";
    }
}
