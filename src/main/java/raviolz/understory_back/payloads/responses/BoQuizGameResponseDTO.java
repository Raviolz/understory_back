package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.QuizGame;
import raviolz.understory_back.enums.QuizAnswerOption;

import java.util.UUID;

public record BoQuizGameResponseDTO(
        UUID id,
        UUID experienceId,
        String experienceTitle,
        String cityName,
        String pointOfInterestName,
        String questionText,
        String answerA,
        String answerB,
        String answerC,
        String answerD,
        QuizAnswerOption correctAnswer,
        String explanationText
) {
    public static BoQuizGameResponseDTO fromEntity(QuizGame quizGame) {
        return new BoQuizGameResponseDTO(
                quizGame.getId(),
                quizGame.getExperience().getId(),
                quizGame.getExperience().getTitle(),
                quizGame.getExperience().getPointOfInterest().getCity().getName(),
                quizGame.getExperience().getPointOfInterest().getName(),
                quizGame.getQuestionText(),
                quizGame.getAnswerA(),
                quizGame.getAnswerB(),
                quizGame.getAnswerC(),
                quizGame.getAnswerD(),
                quizGame.getCorrectAnswer(),
                quizGame.getExplanationText()
        );
    }
}