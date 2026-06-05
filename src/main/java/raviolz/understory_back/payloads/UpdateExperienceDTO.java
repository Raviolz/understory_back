package raviolz.understory_back.payloads;

import jakarta.validation.constraints.*;
import raviolz.understory_back.enums.GameType;

import java.util.UUID;

public record UpdateExperienceDTO(
        @NotNull(message = "Point of interest ID is required")
        UUID pointOfInterestId,

        @NotNull(message = "Experience category ID is required")
        UUID experienceCategoryId,

        @NotBlank(message = "Experience title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @NotNull(message = "Game type is required")
        GameType gameType,

        @NotBlank(message = "Hook text is required")
        @Size(max = 1000, message = "Hook text cannot exceed 1000 characters")
        String hookText,

        @NotBlank(message = "Intro text is required")
        @Size(max = 3000, message = "Intro text cannot exceed 3000 characters")
        String introText,

        @Size(max = 3000, message = "Context text cannot exceed 3000 characters")
        String contextText,

        @Size(max = 3000, message = "Lead-in text cannot exceed 3000 characters")
        String leadInText,

        @NotBlank(message = "Reveal title is required")
        @Size(max = 300, message = "Reveal title cannot exceed 300 characters")
        String revealTitle,

        @NotBlank(message = "Reveal text is required")
        @Size(max = 3000, message = "Reveal text cannot exceed 3000 characters")
        String revealText,

        @NotBlank(message = "Journal text is required")
        @Size(max = 1000, message = "Journal text cannot exceed 1000 characters")
        String journalText,

        @Size(max = 10000, message = "Atlas text cannot exceed 10000 characters")
        String atlasText,

        @NotNull(message = "XP reward is required")
        @Min(value = 1, message = "XP reward must be greater than zero")
        Integer xpReward,

        @NotNull(message = "Difficulty is required")
        @Min(value = 1, message = "Difficulty must be between 1 and 5")
        @Max(value = 5, message = "Difficulty must be between 1 and 5")
        Integer difficulty
) {
}