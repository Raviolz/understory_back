package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "upload_games")
public class UploadGame {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "experience_id", nullable = false, unique = true)
    private Experience experience;

    @Column(name = "prompt_text", nullable = false, length = 1000)
    private String promptText;

    @Column(name = "validation_hint", length = 1000)
    private String validationHint;

    @Column(name = "target_description", length = 1000, nullable = false)
    private String targetDescription;

    @Column(name = "reference_image_url")
    private String referenceImageUrl;

    public UploadGame(Experience experience, String promptText, String validationHint, String targetDescription, String referenceImageUrl
    ) {
        setExperience(experience);
        setPromptText(promptText);
        setValidationHint(validationHint);
        setTargetDescription(targetDescription);
        setReferenceImageUrl(referenceImageUrl);
    }

    private void setExperience(Experience experience) {
        if (experience == null) {
            throw new ValidationException("Experience is required");
        }
        this.experience = experience;
    }

    private void setPromptText(String promptText) {
        if (promptText == null || promptText.isBlank()) {
            throw new ValidationException("Prompt text is required");
        }
        this.promptText = promptText;
    }

    private void setTargetDescription(String targetDescription) {
        if (targetDescription == null || targetDescription.isBlank()) {
            throw new ValidationException("Target description is required");
        }
        this.targetDescription = targetDescription;
    }

    private void setReferenceImageUrl(String referenceImageUrl) {
        this.referenceImageUrl = referenceImageUrl.trim();
    }

    private void setValidationHint(String validationHint) {
        this.validationHint = validationHint;
    }


    @Override
    public String toString() {
        return "UploadGame{" +
                "id=" + id +
                ", experience=" + (experience != null ? experience.getTitle() : "N/A") +
                ", promptText='" + promptText + '\'' +
                ", validationHint='" + validationHint + '\'' +
                ", targetDescription='" + targetDescription + '\'' +
                ", referenceImageUrl='" + referenceImageUrl + '\'' +
                '}';
    }
}
