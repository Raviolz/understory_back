package raviolz.understory_back.payloads;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import raviolz.understory_back.enums.QuizAnswerOption;


public record UpdateQuizGameDTO(
        @NotBlank(message = "Question text is required")
        @Size(max = 1000, message = "Question text cannot exceed 1000 characters")
        String questionText,

        @NotBlank(message = "Answer A is required")
        @Size(max = 500, message = "Answer A cannot exceed 500 characters")
        String answerA,

        @NotBlank(message = "Answer B is required")
        @Size(max = 500, message = "Answer B cannot exceed 500 characters")
        String answerB,

        @NotBlank(message = "Answer C is required")
        @Size(max = 500, message = "Answer C cannot exceed 500 characters")
        String answerC,

        @NotBlank(message = "Answer D is required")
        @Size(max = 500, message = "Answer D cannot exceed 500 characters")
        String answerD,

        @NotNull(message = "Correct answer is required")
        QuizAnswerOption correctAnswer,

        @NotBlank(message = "Explanation text is required")
        @Size(max = 1500, message = "Explanation text cannot exceed 1500 characters")
        String explanationText
) {
}