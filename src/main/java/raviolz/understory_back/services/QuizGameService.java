package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.QuizGame;
import raviolz.understory_back.enums.GameType;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.QuizGameDTO;
import raviolz.understory_back.payloads.UpdateQuizGameDTO;
import raviolz.understory_back.repositories.QuizGameRepository;
import raviolz.understory_back.repositories.UserExperienceProgressRepository;
import raviolz.understory_back.repositories.UserUploadSubmissionRepository;

import java.util.UUID;

@Service
public class QuizGameService {

    private final QuizGameRepository quizGameRepository;
    private final ExperienceService experienceService;
    private final UserExperienceProgressRepository userExperienceProgressRepository;
    private final UserUploadSubmissionRepository userUploadSubmissionRepository;

    public QuizGameService(QuizGameRepository quizGameRepository,
                           ExperienceService experienceService,
                           UserExperienceProgressRepository userExperienceProgressRepository,
                           UserUploadSubmissionRepository userUploadSubmissionRepository) {
        this.quizGameRepository = quizGameRepository;
        this.experienceService = experienceService;
        this.userExperienceProgressRepository = userExperienceProgressRepository;
        this.userUploadSubmissionRepository = userUploadSubmissionRepository;
    }

    public QuizGame save(QuizGameDTO body) {
        Experience experience = experienceService.findById(body.experienceId());

        if (experience.getGameType() != GameType.QUIZ) {
            throw new ValidationException("Experience " + body.experienceId() + " is not a quiz experience");
        }

        if (quizGameRepository.existsByExperienceId(body.experienceId())) {
            throw new ValidationException("Quiz game already exists for experience " + body.experienceId());
        }

        QuizGame quizGame = new QuizGame(
                experience,
                body.questionText(),
                body.answerA(),
                body.answerB(),
                body.answerC(),
                body.answerD(),
                body.correctAnswer(),
                body.explanationText()
        );

        return quizGameRepository.save(quizGame);
    }

    public Page<QuizGame> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return quizGameRepository.findAll(pageable);
    }

    public QuizGame findById(UUID id) {
        return quizGameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz game with id " + id + " not found"));
    }

    public QuizGame findByExperienceId(UUID experienceId) {
        return quizGameRepository.findByExperienceId(experienceId)
                .orElseThrow(() -> new NotFoundException("Quiz game for experience " + experienceId + " not found"));
    }

    public QuizGame update(UUID id, UpdateQuizGameDTO body) {
        QuizGame found = findById(id);

        found.setQuestionText(body.questionText());
        found.setAnswerA(body.answerA());
        found.setAnswerB(body.answerB());
        found.setAnswerC(body.answerC());
        found.setAnswerD(body.answerD());
        found.setCorrectAnswer(body.correctAnswer());
        found.setExplanationText(body.explanationText());

        return quizGameRepository.save(found);
    }

    public void delete(UUID id) {
        QuizGame found = findById(id);

        UUID experienceId = found.getExperience().getId();

        if (userExperienceProgressRepository.existsByExperienceId(experienceId)) {
            throw new ValidationException("Cannot delete quiz game linked to an experience with user progress. Unpublish the experience instead.");
        }

        if (userUploadSubmissionRepository.existsByExperienceId(experienceId)) {
            throw new ValidationException("Cannot delete quiz game linked to an experience with upload submissions. Unpublish the experience instead.");
        }

        quizGameRepository.delete(found);
    }
}