package raviolz.understory_back.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDTO(
        @NotNull(message = "User reward ID is required")
        UUID userRewardId,

        @NotNull(message = "Booking date is required")
        @Future(message = "Booking date must be in the future")
        LocalDateTime bookingDate,

        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        String notes,

        @Min(value = 1, message = "People count must be greater than zero")
        int peopleCount
) {
}