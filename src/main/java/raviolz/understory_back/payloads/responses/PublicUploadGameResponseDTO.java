package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UploadGame;

import java.util.UUID;

public record PublicUploadGameResponseDTO(
        UUID id,
        UUID experienceId,
        String promptText,
        String validationHint,
        String targetDescription,
        String referenceImageUrl,
        String explanationText
) {
    public static PublicUploadGameResponseDTO fromEntity(UploadGame uploadGame) {
        return new PublicUploadGameResponseDTO(
                uploadGame.getId(),
                uploadGame.getExperience().getId(),
                uploadGame.getPromptText(),
                uploadGame.getValidationHint(),
                uploadGame.getTargetDescription(),
                uploadGame.getReferenceImageUrl(),
                uploadGame.getExplanationText()
        );
    }
}