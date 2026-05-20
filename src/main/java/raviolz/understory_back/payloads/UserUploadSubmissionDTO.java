package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUploadSubmissionDTO(
        @NotNull(message = "Experience ID is required")
        UUID experienceId,

        @NotBlank(message = "Image URL is required")
        String imageUrl
) {
}