package raviolz.understory_back.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import raviolz.understory_back.enums.RewardType;

import java.time.LocalDate;
import java.util.UUID;

public record RewardDTO(
        @NotNull(message = "Business ID is required")
        UUID businessId,

        @NotNull(message = "Experience ID is required")
        UUID experienceId,

        @NotBlank(message = "Reward title is required")
        @Size(max = 150, message = "Reward title cannot exceed 150 characters")
        String title,

        @NotBlank(message = "Reward description is required")
        @Size(max = 1500, message = "Reward description cannot exceed 1500 characters")
        String description,

        @Size(max = 80, message = "Discount code cannot exceed 80 characters")
        String discountCode,

        @NotNull(message = "Reward type is required")
        RewardType rewardType,

        @NotNull(message = "Valid from date is required")
        @FutureOrPresent(message = "Valid from date cannot be in the past") // controlla che sia o oggi o nel futuro
        LocalDate validFrom,
// la validazione tra le due date: form e until la controlla l' entity
        @NotNull(message = "Valid until date is required")
        @FutureOrPresent(message = "Valid until date cannot be in the past")
        LocalDate validUntil
) {
}