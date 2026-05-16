package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.ExperienceCategory;
import raviolz.understory_back.payloads.ExperienceCategoryDTO;
import raviolz.understory_back.payloads.UpdateExperienceCategoryDTO;
import raviolz.understory_back.services.ExperienceCategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/experience-categories")
public class BoExperienceCategoryController {

    private final ExperienceCategoryService experienceCategoryService;

    public BoExperienceCategoryController(ExperienceCategoryService experienceCategoryService) {
        this.experienceCategoryService = experienceCategoryService;
    }

    @GetMapping
    public Page<ExperienceCategory> findAll(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "label") String sortBy) {
        return experienceCategoryService.findAll(page, size, sortBy);
    }

    @GetMapping("/{categoryId}")
    public ExperienceCategory findById(@PathVariable UUID categoryId) {
        return experienceCategoryService.findById(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExperienceCategory save(@RequestBody @Valid ExperienceCategoryDTO body) {
        return experienceCategoryService.save(body);
    }

    @PutMapping("/{categoryId}")
    public ExperienceCategory update(@PathVariable UUID categoryId,
                                     @RequestBody @Valid UpdateExperienceCategoryDTO body) {
        return experienceCategoryService.update(categoryId, body);
    }
}