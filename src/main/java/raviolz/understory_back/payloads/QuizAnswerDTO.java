package raviolz.understory_back.payloads;

import jakarta.validation.constraints.NotNull;
import raviolz.understory_back.enums.QuizAnswerOption;

import java.util.UUID;

public record QuizAnswerDTO(
        // TODO auth:con JWT, userId lo prendo dall'utente autenticato e non dal body.
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotNull(message = "Experience ID is required")
        UUID experienceId,

        @NotNull(message = "Selected answer is required")
        QuizAnswerOption selectedAnswer
) {
}
