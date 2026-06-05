package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.enums.GameType;

import java.util.UUID;

public record BoExperienceResponseDTO(
        UUID id,

        UUID pointOfInterestId,
        String pointOfInterestName,
        UUID cityId,
        String cityName,

        UUID experienceCategoryId,
        String experienceCategoryCode,
        String experienceCategoryLabel,
        String experienceCategoryIcon,
        String experienceCategoryColor,

        String title,
        GameType gameType,

        String hookText,
        String introText,
        String contextText,
        String leadInText,

        String revealTitle,
        String revealImageUrl,
        String revealText,
        String journalText,
        String atlasText,

        int xpReward,
        int difficulty,
        boolean active
) {
    public static BoExperienceResponseDTO fromEntity(Experience experience) {
        return new BoExperienceResponseDTO(
                experience.getId(),

                experience.getPointOfInterest().getId(),
                experience.getPointOfInterest().getName(),
                experience.getPointOfInterest().getCity().getId(),
                experience.getPointOfInterest().getCity().getName(),

                experience.getExperienceCategory().getId(),
                experience.getExperienceCategory().getCode(),
                experience.getExperienceCategory().getLabel(),
                experience.getExperienceCategory().getIcon(),
                experience.getExperienceCategory().getColor(),

                experience.getTitle(),
                experience.getGameType(),

                experience.getHookText(),
                experience.getIntroText(),
                experience.getContextText(),
                experience.getLeadInText(),

                experience.getRevealTitle(),
                experience.getRevealImageUrl(),
                experience.getRevealText(),
                experience.getJournalText(),
                experience.getAtlasText(),


                experience.getXpReward(),
                experience.getDifficulty(),
                experience.isActive()
        );
    }
}