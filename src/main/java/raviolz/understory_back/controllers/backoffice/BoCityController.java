package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.payloads.CityDTO;
import raviolz.understory_back.payloads.UpdateCityDTO;
import raviolz.understory_back.payloads.responses.CityResponseDTO;
import raviolz.understory_back.services.CityService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/cities")
public class BoCityController {

    private final CityService cityService;

    public BoCityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public Page<CityResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "name") String sortBy) {
        return cityService.findAll(page, size, sortBy)
                .map(CityResponseDTO::fromEntity);
    }

    @GetMapping("/{cityId}")
    public CityResponseDTO findById(@PathVariable UUID cityId) {
        return CityResponseDTO.fromEntity(cityService.findById(cityId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityResponseDTO save(@RequestBody @Valid CityDTO body) {
        return CityResponseDTO.fromEntity(cityService.save(body));
    }

    @PutMapping("/{cityId}")
    public CityResponseDTO update(@PathVariable UUID cityId,
                                  @RequestBody @Valid UpdateCityDTO body) {
        return CityResponseDTO.fromEntity(cityService.update(cityId, body));
    }


    @PatchMapping(value = "/{cityId}/cover-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CityResponseDTO updateCoverImage(@PathVariable UUID cityId,
                                            @RequestParam("file") MultipartFile file) {
        return CityResponseDTO.fromEntity(cityService.updateCoverImage(cityId, file));
    }

    @PatchMapping("/{cityId}/publish")
    public CityResponseDTO publish(@PathVariable UUID cityId) {
        return CityResponseDTO.fromEntity(cityService.publish(cityId));
    }

    @PatchMapping("/{cityId}/unpublish")
    public CityResponseDTO unpublish(@PathVariable UUID cityId) {
        return CityResponseDTO.fromEntity(cityService.unpublish(cityId));
    }

    @DeleteMapping("/{cityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID cityId) {
        cityService.delete(cityId);
    }
}