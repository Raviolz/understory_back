package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBusinessCategoryDTO(
        @NotBlank(message = "Business category label is required")
        @Size(max = 50, message = "Business category label cannot exceed 50 characters")
        String label,

        @NotBlank(message = "Business category description is required")
        @Size(max = 500, message = "Business category description cannot exceed 500 characters")
        String description,

        @NotBlank(message = "Business category icon is required")
        @Size(max = 100, message = "Business category icon cannot exceed 100 characters")
        String icon
) {
}