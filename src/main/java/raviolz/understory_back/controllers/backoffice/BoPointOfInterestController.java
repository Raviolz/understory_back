package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.payloads.PointOfInterestDTO;
import raviolz.understory_back.payloads.UpdatePointOfInterestDTO;
import raviolz.understory_back.payloads.responses.PointOfInterestResponseDTO;
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
    public Page<PointOfInterestResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "name") String sortBy) {
        return pointOfInterestService.findAll(page, size, sortBy)
                .map(PointOfInterestResponseDTO::fromEntity);
    }

    @GetMapping("/{pointId}")
    public PointOfInterestResponseDTO findById(@PathVariable UUID pointId) {
        return PointOfInterestResponseDTO.fromEntity(pointOfInterestService.findById(pointId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PointOfInterestResponseDTO save(@RequestBody @Valid PointOfInterestDTO body) {
        return PointOfInterestResponseDTO.fromEntity(pointOfInterestService.save(body));
    }

    @PutMapping("/{pointId}")
    public PointOfInterestResponseDTO update(@PathVariable UUID pointId,
                                             @RequestBody @Valid UpdatePointOfInterestDTO body) {
        return PointOfInterestResponseDTO.fromEntity(pointOfInterestService.update(pointId, body));
    }

    @PatchMapping(value = "/{pointId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PointOfInterestResponseDTO updateImage(@PathVariable UUID pointId,
                                                  @RequestParam("file") MultipartFile file) {
        return PointOfInterestResponseDTO.fromEntity(
                pointOfInterestService.updateImage(pointId, file)
        );
    }

    @PatchMapping("/{pointId}/publish")
    public PointOfInterestResponseDTO publish(@PathVariable UUID pointId) {
        return PointOfInterestResponseDTO.fromEntity(pointOfInterestService.publish(pointId));
    }

    @PatchMapping("/{pointId}/unpublish")
    public PointOfInterestResponseDTO unpublish(@PathVariable UUID pointId) {
        return PointOfInterestResponseDTO.fromEntity(pointOfInterestService.unpublish(pointId));
    }
}