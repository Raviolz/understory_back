package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UploadGame;

import java.util.UUID;

public record BoUploadGameResponseDTO(
        UUID id,
        UUID experienceId,
        String experienceTitle,
        String cityName,
        String pointOfInterestName,
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
                uploadGame.getExperience().getPointOfInterest().getCity().getName(),
                uploadGame.getExperience().getPointOfInterest().getName(),
                uploadGame.getPromptText(),
                uploadGame.getValidationHint(),
                uploadGame.getTargetDescription(),
                uploadGame.getReferenceImageUrl(),
                uploadGame.getExplanationText()
        );
    }
}