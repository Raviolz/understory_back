package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.enums.GameType;

import java.util.UUID;

public record ExperienceResponseDTO(UUID id, UUID pointOfInterestId, UUID experienceCategoryId,
                                    String experienceCategoryCode, String experienceCategoryLabel,
                                    String experienceCategoryIcon, String experienceCategoryColor, String title,
                                    GameType gameType, String hookText, String introText, String contextText,
                                    String leadInText,
                                    int xpReward, int difficulty
) {
    public static ExperienceResponseDTO fromEntity(Experience experience) {
        return new ExperienceResponseDTO(
                experience.getId(),
                experience.getPointOfInterest().getId(),
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
                experience.getXpReward(),
                experience.getDifficulty()
        );
    }
}