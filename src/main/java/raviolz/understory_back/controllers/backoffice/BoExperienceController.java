package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.QuizGame;
import raviolz.understory_back.entities.UploadGame;
import raviolz.understory_back.payloads.ExperienceDTO;
import raviolz.understory_back.payloads.UpdateExperienceDTO;
import raviolz.understory_back.services.ExperienceService;
import raviolz.understory_back.services.QuizGameService;
import raviolz.understory_back.services.UploadGameService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/experiences")
public class BoExperienceController {

    private final ExperienceService experienceService;
    private final QuizGameService quizGameService;
    private final UploadGameService uploadGameService;

    public BoExperienceController(ExperienceService experienceService, QuizGameService quizGameService, UploadGameService uploadGameService) {
        this.experienceService = experienceService;
        this.quizGameService = quizGameService;
        this.uploadGameService = uploadGameService;
    }

    @GetMapping
    public Page<Experience> findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "title") String sortBy) {
        return experienceService.findAll(page, size, sortBy);
    }

    @GetMapping("/{experienceId}")
    public Experience findById(@PathVariable UUID experienceId) {
        return experienceService.findById(experienceId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Experience save(@RequestBody @Valid ExperienceDTO body) {
        return experienceService.save(body);
    }

    @PutMapping("/{experienceId}")
    public Experience update(@PathVariable UUID experienceId,
                             @RequestBody @Valid UpdateExperienceDTO body) {
        return experienceService.update(experienceId, body);
    }


    @GetMapping("/{experienceId}/quiz-game")
    public QuizGame findQuizGameByExperience(@PathVariable UUID experienceId) {
        return quizGameService.findByExperienceId(experienceId);
    }

    @GetMapping("/{experienceId}/upload-game")
    public UploadGame findUploadGameByExperience(@PathVariable UUID experienceId) {
        return uploadGameService.findByExperienceId(experienceId);
    }

    @PatchMapping("/{experienceId}/publish")
    public Experience publish(@PathVariable UUID experienceId) {
        return experienceService.publish(experienceId);
    }

    @PatchMapping("/{experienceId}/unpublish")
    public Experience unpublish(@PathVariable UUID experienceId) {
        return experienceService.unpublish(experienceId);
    }
}
