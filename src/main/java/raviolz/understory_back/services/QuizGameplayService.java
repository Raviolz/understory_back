package raviolz.understory_back.services;

import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.QuizGame;
import raviolz.understory_back.entities.UserReward;
import raviolz.understory_back.enums.GameType;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.QuizAnswerDTO;
import raviolz.understory_back.payloads.responses.QuizAnswerResponseDTO;

import java.util.Optional;
import java.util.UUID;

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

    public QuizAnswerResponseDTO submitAnswer(UUID userId, QuizAnswerDTO body) {
        userService.findById(userId);

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
                    false,
                    null,
                    "Wrong answer. Try again.",
                    null
            );
        }
// QuizGameplayS controlla se e' giusta, se lo e': UserExperienceProgressS completa l experience e assegna XP solo se non erano già stati assegnati.
        int xpGained = userExperienceProgressService.completeAndAwardXp(
                userId,
                body.experienceId()
        );

        Optional<UserReward> unlockedReward = xpGained > 0
                ? userRewardService.unlockRandomRewardForExperienceCity(
                userId,
                body.experienceId()
        )
                : Optional.empty();

        String message = xpGained > 0
                ? "Correct answer. Experience completed."
                : "Correct answer. Experience was already completed.";

        return new QuizAnswerResponseDTO(
                true,
                true,
                xpGained,
                unlockedReward.isPresent(),
                unlockedReward.map(userReward -> userReward.getReward().getTitle()).orElse(null),
                message,
                quizGame.getExplanationText()
        );
    }
}