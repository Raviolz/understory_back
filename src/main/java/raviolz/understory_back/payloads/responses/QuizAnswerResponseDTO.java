package raviolz.understory_back.payloads.responses;

public record QuizAnswerResponseDTO(
        boolean correct,
        boolean completed,
        int xpAwarded,
        boolean rewardUnlocked,
        String rewardTitle,
        String message,
        String explanationText
) {
}