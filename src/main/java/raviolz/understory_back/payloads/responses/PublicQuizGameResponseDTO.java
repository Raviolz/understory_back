package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.QuizGame;

import java.util.UUID;

public record PublicQuizGameResponseDTO(
        UUID id,
        UUID experienceId,
        String questionText,
        String answerA,
        String answerB,
        String answerC,
        String answerD
) {
    public static PublicQuizGameResponseDTO fromEntity(QuizGame quizGame) {
        return new PublicQuizGameResponseDTO(
                quizGame.getId(),
                quizGame.getExperience().getId(),
                quizGame.getQuestionText(),
                quizGame.getAnswerA(),
                quizGame.getAnswerB(),
                quizGame.getAnswerC(),
                quizGame.getAnswerD()
        );
    }
}