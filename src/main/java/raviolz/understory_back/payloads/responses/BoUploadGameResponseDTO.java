package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UploadGame;

import java.util.UUID;

public record BoUploadGameResponseDTO(
        UUID id,
        UUID experienceId,
        String experienceTitle,
        String promptText,
        String validationHint,
        String targetDescription,
        String referenceImageUrl,
        String explanationText
) {
    public static BoUploadGameResponseDTO fromEntity(UploadGame uploadGame) {
        return new BoUploadGameResponseDTO(
                uploadGame.getId(),
                uploadGame.getExperience().getId(),
                uploadGame.getExperience().getTitle(),
                uploadGame.getPromptText(),
                uploadGame.getValidationHint(),
                uploadGame.getTargetDescription(),
                uploadGame.getReferenceImageUrl(),
                uploadGame.getExplanationText()
        );
    }
}