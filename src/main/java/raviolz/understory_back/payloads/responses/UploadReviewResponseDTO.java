package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.enums.UploadSubmissionStatus;

import java.util.UUID;

public record UploadReviewResponseDTO(
        UUID submissionId,
        UUID userId,
        UUID experienceId,
        UploadSubmissionStatus status,
        boolean completed,
        int xpGained,
        String message
) {
}