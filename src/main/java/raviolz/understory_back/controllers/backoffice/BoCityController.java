package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.City;
import raviolz.understory_back.payloads.CityDTO;
import raviolz.understory_back.payloads.UpdateCityDTO;
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
    public Page<City> findAll(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "name") String sortBy) {
        return cityService.findAll(page, size, sortBy);
    }

    @GetMapping("/{cityId}")
    public City findById(@PathVariable UUID cityId) {
        return cityService.findById(cityId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public City save(@RequestBody @Valid CityDTO body) {
        return cityService.save(body);
    }

    @PutMapping("/{cityId}")
    public City update(@PathVariable UUID cityId,
                       @RequestBody @Valid UpdateCityDTO body) {
        return cityService.update(cityId, body);
    }

    @PatchMapping("/{cityId}/publish")
    public City publish(@PathVariable UUID cityId) {
        return cityService.publish(cityId);
    }

    @PatchMapping("/{cityId}/unpublish")
    public City unpublish(@PathVariable UUID cityId) {
        return cityService.unpublish(cityId);
    }
}