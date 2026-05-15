package raviolz.understory_back.payloads;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record UpdateLocalBusinessDTO(
        @NotNull(message = "City ID is required")
        UUID cityId,

        @NotNull(message = "Business category ID is required")
        UUID businessCategoryId,

        @NotBlank(message = "Business name is required")
        @Size(max = 150, message = "Business name cannot exceed 150 characters")
        String name,

        @NotBlank(message = "Business address is required")
        @Size(max = 255, message = "Business address cannot exceed 255 characters")
        String address,

        @NotBlank(message = "Business description is required")
        @Size(max = 1500, message = "Business description cannot exceed 1500 characters")
        String description,

        String websiteUrl,

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180 degrees")
        @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180 degrees")
        Double longitude,

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90 degrees")
        @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90 degrees")
        Double latitude
) {
}