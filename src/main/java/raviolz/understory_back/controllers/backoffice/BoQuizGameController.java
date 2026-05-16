package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.QuizGame;
import raviolz.understory_back.payloads.QuizGameDTO;
import raviolz.understory_back.payloads.UpdateQuizGameDTO;
import raviolz.understory_back.services.QuizGameService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/quiz-games")
public class BoQuizGameController {

    private final QuizGameService quizGameService;

    public BoQuizGameController(QuizGameService quizGameService) {
        this.quizGameService = quizGameService;
    }

    @GetMapping
    public Page<QuizGame> findAll(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "questionText") String sortBy) {
        return quizGameService.findAll(page, size, sortBy);
    }

    @GetMapping("/{quizGameId}")
    public QuizGame findById(@PathVariable UUID quizGameId) {
        return quizGameService.findById(quizGameId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuizGame save(@RequestBody @Valid QuizGameDTO body) {
        return quizGameService.save(body);
    }

    @PutMapping("/{quizGameId}")
    public QuizGame update(@PathVariable UUID quizGameId,
                           @RequestBody @Valid UpdateQuizGameDTO body) {
        return quizGameService.update(quizGameId, body);
    }
}