package raviolz.understory_back.controllers.publicapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.services.ExperienceService;

import java.util.UUID;

@RestController
@RequestMapping("/api/experiences")
public class PublicExperienceController {

    private final ExperienceService experienceService;

    public PublicExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping("/{experienceId}")
    public Experience findActiveById(@PathVariable UUID experienceId) {
        return experienceService.findActiveById(experienceId);
    }
}