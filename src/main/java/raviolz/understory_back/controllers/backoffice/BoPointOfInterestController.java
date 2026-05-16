package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.PointOfInterest;
import raviolz.understory_back.payloads.PointOfInterestDTO;
import raviolz.understory_back.payloads.UpdatePointOfInterestDTO;
import raviolz.understory_back.services.PointOfInterestService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/points")
public class BoPointOfInterestController {

    private final PointOfInterestService pointOfInterestService;

    public BoPointOfInterestController(PointOfInterestService pointOfInterestService) {
        this.pointOfInterestService = pointOfInterestService;
    }

    @GetMapping
    public Page<PointOfInterest> findAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "name") String sortBy) {
        return pointOfInterestService.findAll(page, size, sortBy);
    }

    @GetMapping("/{pointId}")
    public PointOfInterest findById(@PathVariable UUID pointId) {
        return pointOfInterestService.findById(pointId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PointOfInterest save(@RequestBody @Valid PointOfInterestDTO body) {
        return pointOfInterestService.save(body);
    }

    @PutMapping("/{pointId}")
    public PointOfInterest update(@PathVariable UUID pointId,
                                  @RequestBody @Valid UpdatePointOfInterestDTO body) {
        return pointOfInterestService.update(pointId, body);
    }

    @PatchMapping("/{pointId}/publish")
    public PointOfInterest publish(@PathVariable UUID pointId) {
        return pointOfInterestService.publish(pointId);
    }

    @PatchMapping("/{pointId}/unpublish")
    public PointOfInterest unpublish(@PathVariable UUID pointId) {
        return pointOfInterestService.unpublish(pointId);
    }
}