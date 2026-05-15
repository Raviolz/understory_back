package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserExperienceProgressDTO(
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotNull(message = "Experience ID is required")
        UUID experienceId
) {
}