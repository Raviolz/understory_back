package raviolz.understory_back.services;

import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.QuizGame;
import raviolz.understory_back.enums.GameType;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.QuizAnswerDTO;
import raviolz.understory_back.payloads.responses.QuizAnswerResponseDTO;

@Service
public class QuizGameplayService {

    private final UserService userService;
    private final ExperienceService experienceService;
    private final QuizGameService quizGameService;
    private final UserExperienceProgressService userExperienceProgressService;
    private final UserRewardService userRewardService;

    public QuizGameplayService(
            UserService userService,
            ExperienceService experienceService,
            QuizGameService quizGameService,
            UserExperienceProgressService userExperienceProgressService,
            UserRewardService userRewardService
    ) {
        this.userService = userService;
        this.experienceService = experienceService;
        this.quizGameService = quizGameService;
        this.userExperienceProgressService = userExperienceProgressService;
        this.userRewardService = userRewardService;
    }

    public QuizAnswerResponseDTO submitAnswer(QuizAnswerDTO body) {
        userService.findById(body.userId());

        Experience experience = experienceService.findById(body.experienceId());

        if (experience.getGameType() != GameType.QUIZ) {
            throw new ValidationException("Experience " + body.experienceId() + " is not a quiz experience");
        }

        QuizGame quizGame = quizGameService.findByExperienceId(body.experienceId());

        if (body.selectedAnswer() != quizGame.getCorrectAnswer()) {
            return new QuizAnswerResponseDTO(
                    false,
                    false,
                    0,
                    "Wrong answer. Try again.",
                    null
            );
        }
// QuizGameplayS controlla se e' giusta, se lo e': UserExperienceProgressS completa l experience e assegna XP solo se non erano già stati assegnati.
        int xpGained = userExperienceProgressService.completeAndAwardXp(
                body.userId(),
                body.experienceId()
        );

        if (xpGained > 0) {
            userRewardService.unlockRandomRewardForExperienceCity(
                    body.userId(),
                    body.experienceId()
            );
        }

        String message = xpGained > 0
                ? "Correct answer. Experience completed."
                : "Correct answer. Experience was already completed.";

        return new QuizAnswerResponseDTO(
                true,
                true,
                xpGained,
                message,
                quizGame.getExplanationText()
        );
    }
}