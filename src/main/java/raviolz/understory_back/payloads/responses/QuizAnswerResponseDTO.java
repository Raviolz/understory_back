package raviolz.understory_back.payloads.responses;

public record QuizAnswerResponseDTO(
        boolean correct,
        boolean completed,
        int xpAwarded,
        String message,
        String explanationText
) {
}