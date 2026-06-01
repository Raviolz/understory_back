package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UploadGameDTO(
        @NotNull(message = "Experience ID is required")
        UUID experienceId,

        @NotBlank(message = "Prompt text is required")
        @Size(max = 1000, message = "Prompt text cannot exceed 1000 characters")
        String promptText,

        @Size(max = 1000, message = "Validation hint cannot exceed 1000 characters")
        String validationHint,

        @NotBlank(message = "Target description is required")
        @Size(max = 1000, message = "Target description cannot exceed 1000 characters")
        String targetDescription,

        @Size(max = 3000, message = "Explanation text cannot exceed 3000 characters")
        String explanationText
) {
}