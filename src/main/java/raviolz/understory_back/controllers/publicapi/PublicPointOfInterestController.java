package raviolz.understory_back.controllers.publicapi;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.payloads.responses.ExperienceResponseDTO;
import raviolz.understory_back.payloads.responses.PointOfInterestResponseDTO;
import raviolz.understory_back.services.ExperienceService;
import raviolz.understory_back.services.PointOfInterestService;

import java.util.UUID;

@RestController
@RequestMapping("/points")
public class PublicPointOfInterestController {

    private final PointOfInterestService pointOfInterestService;
    private final ExperienceService experienceService;

    public PublicPointOfInterestController(PointOfInterestService pointOfInterestService, ExperienceService experienceService) {
        this.pointOfInterestService = pointOfInterestService;
        this.experienceService = experienceService;
    }

    @GetMapping("/{pointId}")
    public PointOfInterestResponseDTO findActiveById(@PathVariable UUID pointId) {
        return PointOfInterestResponseDTO.fromEntity(pointOfInterestService.findActiveById(pointId));
    }

    @GetMapping("/{pointId}/experiences")
    public Page<ExperienceResponseDTO> findActiveExperiencesByPoint(@PathVariable UUID pointId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "title") String sortBy) {
        return experienceService.findActiveByPointOfInterest(pointId, page, size, sortBy)
                .map(experience -> ExperienceResponseDTO.fromEntity(experience));
    }
}