package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRoleDTO(
        @NotBlank(message = "Role label is required")
        @Size(max = 100, message = "Role label cannot exceed 100 characters")
        String label,

        @NotBlank(message = "Role description is required")
        @Size(max = 200, message = "Role description cannot exceed 200 characters")
        String description
) {
}