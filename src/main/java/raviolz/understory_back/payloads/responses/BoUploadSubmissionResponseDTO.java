package raviolz.understory_back.payloads.responses;

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
        UploadSubmissionStatus status,
        LocalDateTime submittedAt
) {
    public static BoUploadSubmissionResponseDTO fromEntity(UserUploadSubmission submission) {
        return new BoUploadSubmissionResponseDTO(
                submission.getId(),
                submission.getUser().getId(),
                submission.getUser().getUsername(),
                submission.getExperience().getId(),
                submission.getExperience().getTitle(),
                submission.getImageUrl(),
                submission.getStatus(),
                submission.getSubmittedAt()
        );
    }
}