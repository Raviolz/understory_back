package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UserExperienceProgress;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserJournalEntryResponseDTO(
        UUID progressId,
        UUID experienceId,
        String experienceTitle,
        UUID pointOfInterestId,
        String pointOfInterestName,
        UUID cityId,
        String cityName,
        String revealTitle,
        String revealImageUrl,
        String revealText,
        String journalText,
        String userNote,
        LocalDateTime completedAt
) {
    public static UserJournalEntryResponseDTO fromEntity(UserExperienceProgress progress) {
        return new UserJournalEntryResponseDTO(
                progress.getId(),
                progress.getExperience().getId(),
                progress.getExperience().getTitle(),
                progress.getExperience().getPointOfInterest().getId(),
                progress.getExperience().getPointOfInterest().getName(),
                progress.getExperience().getPointOfInterest().getCity().getId(),
                progress.getExperience().getPointOfInterest().getCity().getName(),
                progress.getExperience().getRevealTitle(),
                progress.getExperience().getRevealImageUrl(),
                progress.getExperience().getRevealText(),
                progress.getExperience().getJournalText(),
                progress.getUserNote(),
                progress.getCompletedAt()
        );
    }
}
