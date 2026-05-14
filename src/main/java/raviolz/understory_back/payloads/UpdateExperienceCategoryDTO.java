package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateExperienceCategoryDTO(
        @NotBlank(message = "Experience category label is required")
        @Size(max = 50, message = "Experience category label cannot exceed 50 characters")
        String label,

        @NotBlank(message = "Experience category description is required")
        @Size(max = 255, message = "Experience category description cannot exceed 255 characters")
        String description,

        @NotBlank(message = "Experience category icon is required")
        @Size(max = 50, message = "Experience category icon cannot exceed 50 characters")
        String icon,

        @NotBlank(message = "Experience category color is required")
        @Size(max = 20, message = "Experience category color cannot exceed 20 characters")
        String color
) {
}