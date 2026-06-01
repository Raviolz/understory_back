package raviolz.understory_back.controllers.gameplay;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.payloads.QuizAnswerDTO;
import raviolz.understory_back.payloads.responses.QuizAnswerResponseDTO;
import raviolz.understory_back.services.QuizGameplayService;


@RestController
@RequestMapping("/gameplay")
public class QuizGameplayController {

    private final QuizGameplayService quizGameplayService;

    public QuizGameplayController(QuizGameplayService quizGameplayService) {
        this.quizGameplayService = quizGameplayService;
    }

    @PostMapping("/quiz-answer")
    @ResponseStatus(HttpStatus.OK)
    public QuizAnswerResponseDTO submitAnswer(@AuthenticationPrincipal User currentUser,
                                              @RequestBody @Valid QuizAnswerDTO body) {
        return quizGameplayService.submitAnswer(currentUser.getId(), body);
    }
}
