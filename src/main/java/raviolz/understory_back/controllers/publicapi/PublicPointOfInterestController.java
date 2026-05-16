package raviolz.understory_back.controllers.publicapi;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.PointOfInterest;
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
    public PointOfInterest findActiveById(@PathVariable UUID pointId) {
        return pointOfInterestService.findActiveById(pointId);
    }

    @GetMapping("/{pointId}/experiences")
    public Page<Experience> findActiveExperiencesByPoint(@PathVariable UUID pointId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "title") String sortBy) {
        return experienceService.findActiveByPointOfInterest(pointId, page, size, sortBy);
    }
}