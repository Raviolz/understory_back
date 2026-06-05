package raviolz.understory_back.payloads.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExperienceCompletionResponseDTO(
        UUID experienceId,
        boolean completed,
        LocalDateTime completedAt,
        String revealTitle,
        String revealImageUrl,
        String revealText,
        String journalText,
        String explanationText
) {
}