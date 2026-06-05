package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UserExperienceProgress;
import raviolz.understory_back.enums.GameType;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserAtlasEntryResponseDTO(
        UUID progressId,

        UUID experienceId,
        String experienceTitle,
        GameType gameType,

        UUID cityId,
        String cityName,

        UUID pointOfInterestId,
        String pointOfInterestName,
        String pointOfInterestImageUrl,

        UUID experienceCategoryId,
        String experienceCategoryCode,
        String experienceCategoryLabel,
        String experienceCategoryIcon,
        String experienceCategoryColor,

        String revealTitle,
        String revealImageUrl,
        String atlasText,

        String revealText,
        String journalText,
        String explanationText,

        String userNote,
        LocalDateTime completedAt
) {
    public static UserAtlasEntryResponseDTO fromEntity(UserExperienceProgress progress, String explanationText) {
        return new UserAtlasEntryResponseDTO(
                progress.getId(),

                progress.getExperience().getId(),
                progress.getExperience().getTitle(),
                progress.getExperience().getGameType(),

                progress.getExperience().getPointOfInterest().getCity().getId(),
                progress.getExperience().getPointOfInterest().getCity().getName(),

                progress.getExperience().getPointOfInterest().getId(),
                progress.getExperience().getPointOfInterest().getName(),
                progress.getExperience().getPointOfInterest().getImageUrl(),

                progress.getExperience().getExperienceCategory().getId(),
                progress.getExperience().getExperienceCategory().getCode(),
                progress.getExperience().getExperienceCategory().getLabel(),
                progress.getExperience().getExperienceCategory().getIcon(),
                progress.getExperience().getExperienceCategory().getColor(),

                progress.getExperience().getRevealTitle(),
                progress.getExperience().getRevealImageUrl(),
                progress.getExperience().getAtlasText(),

                progress.getExperience().getRevealText(),
                progress.getExperience().getJournalText(),
                explanationText,

                progress.getUserNote(),
                progress.getCompletedAt()
        );
    }
}