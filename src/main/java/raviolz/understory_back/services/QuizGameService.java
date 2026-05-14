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

import java.util.UUID;

@Service
public class QuizGameService {

    private final QuizGameRepository quizGameRepository;
    private final ExperienceService experienceService;

    public QuizGameService(QuizGameRepository quizGameRepository, ExperienceService experienceService) {
        this.quizGameRepository = quizGameRepository;
        this.experienceService = experienceService;
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
}