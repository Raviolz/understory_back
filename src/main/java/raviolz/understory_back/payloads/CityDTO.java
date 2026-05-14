package raviolz.understory_back.payloads;

import jakarta.validation.constraints.*;

public record CityDTO(
        @NotBlank(message = "City name is required")
        @Size(max = 255, message = "City name cannot exceed 255 characters")
        String name,

        @NotBlank(message = "Country is required")
        @Size(max = 255, message = "Country cannot exceed 255 characters")
        String country,

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
        Double longitude,
        // Double wrapper per poter validare il NotNull -->A differenza del tipo primitivo double, Double può essere nullo, quindi posso distinguere
        // un valore mancante da una coordinata valida pari a 0.0.

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
        Double latitude,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        String coverImageUrl
) {
}