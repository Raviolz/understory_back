package raviolz.understory_back.controllers.publicapi;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.payloads.responses.CityResponseDTO;
import raviolz.understory_back.payloads.responses.PointOfInterestResponseDTO;
import raviolz.understory_back.repositories.ExperienceRepository;
import raviolz.understory_back.services.CityService;
import raviolz.understory_back.services.PointOfInterestService;

import java.util.UUID;

@RestController
@RequestMapping("/cities")
public class PublicCityController {

    private final CityService cityService;
    private final PointOfInterestService pointOfInterestService;
    private final ExperienceRepository experienceRepository;

    public PublicCityController(CityService cityService, PointOfInterestService pointOfInterestService, ExperienceRepository experienceRepository) {
        this.cityService = cityService;
        this.pointOfInterestService = pointOfInterestService;
        this.experienceRepository = experienceRepository;
    }

    @GetMapping
    public Page<CityResponseDTO> findAllActive(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "name") String sortBy) {
        return cityService.findActive(page, size, sortBy)
                .map(city -> CityResponseDTO.fromEntityWithDominantCategory(
                        city,
                        cityService.findDominantCategoryByCityId(city.getId())
                ));
    }

    @GetMapping("/{cityId}")
    public CityResponseDTO findActiveById(@PathVariable UUID cityId) {
        return CityResponseDTO.fromEntityWithDominantCategory(
                cityService.findActiveById(cityId),
                cityService.findDominantCategoryByCityId(cityId)
        );
    }

    @GetMapping("/{cityId}/points")
    public Page<PointOfInterestResponseDTO> findActivePointsByCity(@PathVariable UUID cityId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "50") int size,
                                                                   @RequestParam(defaultValue = "name") String sortBy) {
        return pointOfInterestService.findActiveByCity(cityId, page, size, sortBy)
                .map(point -> {
                    Experience primaryExperience = experienceRepository
                            .findFirstByPointOfInterestIdAndActiveTrueOrderByTitleAsc(point.getId())
                            .orElse(null);

                    return PointOfInterestResponseDTO.fromEntity(point, primaryExperience);
                });
    }
}