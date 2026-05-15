package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserRewardDTO(
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotNull(message = "Reward ID is required")
        UUID rewardId
) {
}