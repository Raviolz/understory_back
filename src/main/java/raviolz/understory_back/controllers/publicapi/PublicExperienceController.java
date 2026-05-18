package raviolz.understory_back.controllers.publicapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raviolz.understory_back.payloads.responses.ExperienceResponseDTO;
import raviolz.understory_back.services.ExperienceService;

import java.util.UUID;

@RestController
@RequestMapping("/experiences")
public class PublicExperienceController {

    private final ExperienceService experienceService;

    public PublicExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping("/{experienceId}")
    public ExperienceResponseDTO findActiveById(@PathVariable UUID experienceId) {
        return ExperienceResponseDTO.fromEntity(experienceService.findById(experienceId));
    }
}