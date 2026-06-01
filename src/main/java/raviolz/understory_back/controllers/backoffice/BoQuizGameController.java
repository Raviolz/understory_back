package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.payloads.QuizGameDTO;
import raviolz.understory_back.payloads.UpdateQuizGameDTO;
import raviolz.understory_back.payloads.responses.BoQuizGameResponseDTO;
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
    public Page<BoQuizGameResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "questionText") String sortBy) {
        return quizGameService.findAll(page, size, sortBy)
                .map(BoQuizGameResponseDTO::fromEntity);
    }

    @GetMapping("/{quizGameId}")
    public BoQuizGameResponseDTO findById(@PathVariable UUID quizGameId) {
        return BoQuizGameResponseDTO.fromEntity(
                quizGameService.findById(quizGameId)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoQuizGameResponseDTO save(@RequestBody @Valid QuizGameDTO body) {
        return BoQuizGameResponseDTO.fromEntity(
                quizGameService.save(body)
        );
    }

    @PutMapping("/{quizGameId}")
    public BoQuizGameResponseDTO update(@PathVariable UUID quizGameId,
                                        @RequestBody @Valid UpdateQuizGameDTO body) {
        return BoQuizGameResponseDTO.fromEntity(
                quizGameService.update(quizGameId, body)
        );
    }

    @DeleteMapping("/{quizGameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID quizGameId) {
        quizGameService.delete(quizGameId);
    }
}