package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.entities.QuizGame;
import raviolz.understory_back.entities.UploadGame;
import raviolz.understory_back.payloads.ExperienceDTO;
import raviolz.understory_back.payloads.UpdateExperienceDTO;
import raviolz.understory_back.payloads.responses.BoExperienceResponseDTO;
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

    public BoExperienceController(ExperienceService experienceService,
                                  QuizGameService quizGameService,
                                  UploadGameService uploadGameService) {
        this.experienceService = experienceService;
        this.quizGameService = quizGameService;
        this.uploadGameService = uploadGameService;
    }

    @GetMapping
    public Page<BoExperienceResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "title") String sortBy) {
        return experienceService.findAll(page, size, sortBy)
                .map(BoExperienceResponseDTO::fromEntity);
    }

    @GetMapping("/{experienceId}")
    public BoExperienceResponseDTO findById(@PathVariable UUID experienceId) {
        return BoExperienceResponseDTO.fromEntity(
                experienceService.findById(experienceId)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoExperienceResponseDTO save(@RequestBody @Valid ExperienceDTO body) {
        return BoExperienceResponseDTO.fromEntity(
                experienceService.save(body)
        );
    }

    @PutMapping("/{experienceId}")
    public BoExperienceResponseDTO update(@PathVariable UUID experienceId,
                                          @RequestBody @Valid UpdateExperienceDTO body) {
        return BoExperienceResponseDTO.fromEntity(
                experienceService.update(experienceId, body)
        );
    }

    @PatchMapping(value = "/{experienceId}/reveal-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BoExperienceResponseDTO updateRevealImage(@PathVariable UUID experienceId,
                                                     @RequestParam("file") MultipartFile file) {
        return BoExperienceResponseDTO.fromEntity(
                experienceService.updateRevealImage(experienceId, file)
        );
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
    public BoExperienceResponseDTO publish(@PathVariable UUID experienceId) {
        return BoExperienceResponseDTO.fromEntity(
                experienceService.publish(experienceId)
        );
    }

    @PatchMapping("/{experienceId}/unpublish")
    public BoExperienceResponseDTO unpublish(@PathVariable UUID experienceId) {
        return BoExperienceResponseDTO.fromEntity(
                experienceService.unpublish(experienceId)
        );
    }
}