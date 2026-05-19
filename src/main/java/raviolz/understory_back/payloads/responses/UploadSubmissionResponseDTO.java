package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UserUploadSubmission;
import raviolz.understory_back.enums.UploadSubmissionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UploadSubmissionResponseDTO(
        UUID submissionId,
        UUID userId,
        UUID experienceId,
        String imageUrl,
        UploadSubmissionStatus status,
        LocalDateTime submittedAt
) {
    public static UploadSubmissionResponseDTO fromEntity(UserUploadSubmission submission) {
        return new UploadSubmissionResponseDTO(
                submission.getId(),
                submission.getUser().getId(),
                submission.getExperience().getId(),
                submission.getImageUrl(),
                submission.getStatus(),
                submission.getSubmittedAt()
        );
    }
}
