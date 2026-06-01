package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UploadGame;
import raviolz.understory_back.entities.UserUploadSubmission;
import raviolz.understory_back.enums.UploadSubmissionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record BoUploadSubmissionResponseDTO(
        UUID submissionId,
        UUID userId,
        String username,
        UUID experienceId,
        String experienceTitle,
        String imageUrl,
        String promptText,
        String validationHint,
        String targetDescription,
        String referenceImageUrl,
        UploadSubmissionStatus status,
        LocalDateTime submittedAt
) {
    public static BoUploadSubmissionResponseDTO fromEntity(UserUploadSubmission submission, UploadGame uploadGame) {
        return new BoUploadSubmissionResponseDTO(
                submission.getId(),
                submission.getUser().getId(),
                submission.getUser().getUsername(),
                submission.getExperience().getId(),
                submission.getExperience().getTitle(),
                submission.getImageUrl(),
                uploadGame.getPromptText(),
                uploadGame.getValidationHint(),
                uploadGame.getTargetDescription(),
                uploadGame.getReferenceImageUrl(),
                submission.getStatus(),
                submission.getSubmittedAt()
        );
    }
}