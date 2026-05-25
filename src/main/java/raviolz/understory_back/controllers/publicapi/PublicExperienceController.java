package raviolz.understory_back.controllers.publicapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raviolz.understory_back.payloads.responses.ExperienceResponseDTO;
import raviolz.understory_back.payloads.responses.PublicQuizGameResponseDTO;
import raviolz.understory_back.payloads.responses.PublicUploadGameResponseDTO;
import raviolz.understory_back.services.ExperienceService;
import raviolz.understory_back.services.QuizGameService;
import raviolz.understory_back.services.UploadGameService;

import java.util.UUID;

@RestController
@RequestMapping("/experiences")
public class PublicExperienceController {

    private final ExperienceService experienceService;
    private final QuizGameService quizGameService;
    private final UploadGameService uploadGameService;

    public PublicExperienceController(ExperienceService experienceService, QuizGameService quizGameService, UploadGameService uploadGameService) {
        this.experienceService = experienceService;
        this.quizGameService = quizGameService;
        this.uploadGameService = uploadGameService;
    }

    @GetMapping("/{experienceId}")
    public ExperienceResponseDTO findActiveById(@PathVariable UUID experienceId) {
        return ExperienceResponseDTO.fromEntity(experienceService.findActiveById(experienceId));
    }

    @GetMapping("/{experienceId}/quiz-game")
    public PublicQuizGameResponseDTO findQuizGameByExperience(@PathVariable UUID experienceId) {
        return PublicQuizGameResponseDTO.fromEntity(
                quizGameService.findByExperienceId(experienceId)
        );
    }

    @GetMapping("/{experienceId}/upload-game")
    public PublicUploadGameResponseDTO findUploadGameByExperience(@PathVariable UUID experienceId) {
        return PublicUploadGameResponseDTO.fromEntity(
                uploadGameService.findByExperienceId(experienceId)
        );
    }
}