package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UserExperienceProgress;
import raviolz.understory_back.enums.GameType;
import raviolz.understory_back.enums.ProgressStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProgressResponseDTO(
        UUID progressId,
        UUID experienceId,
        String experienceTitle,
        GameType gameType,
        ProgressStatus status,
        int xpReward,
        boolean xpAwarded,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        String userNote
) {
    public static UserProgressResponseDTO fromEntity(UserExperienceProgress progress) {
        return new UserProgressResponseDTO(
                progress.getId(),
                progress.getExperience().getId(),
                progress.getExperience().getTitle(),
                progress.getExperience().getGameType(),
                progress.getStatus(),
                progress.getExperience().getXpReward(),
                progress.isXpAwarded(),
                progress.getStartedAt(),
                progress.getCompletedAt(),
                progress.getUserNote()
        );
    }
}