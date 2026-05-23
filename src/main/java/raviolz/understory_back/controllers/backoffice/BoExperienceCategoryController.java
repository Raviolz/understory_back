package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.payloads.ExperienceCategoryDTO;
import raviolz.understory_back.payloads.UpdateExperienceCategoryDTO;
import raviolz.understory_back.payloads.responses.ExperienceCategoryResponseDTO;
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
    public Page<ExperienceCategoryResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "label") String sortBy) {
        return experienceCategoryService.findAll(page, size, sortBy)
                .map(ExperienceCategoryResponseDTO::fromEntity);
    }

    @GetMapping("/{categoryId}")
    public ExperienceCategoryResponseDTO findById(@PathVariable UUID categoryId) {
        return ExperienceCategoryResponseDTO.fromEntity(
                experienceCategoryService.findById(categoryId)
        );
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExperienceCategoryResponseDTO save(@RequestBody @Valid ExperienceCategoryDTO body) {
        return ExperienceCategoryResponseDTO.fromEntity(
                experienceCategoryService.save(body)
        );
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping("/{categoryId}")
    public ExperienceCategoryResponseDTO update(@PathVariable UUID categoryId,
                                                @RequestBody @Valid UpdateExperienceCategoryDTO body) {
        return ExperienceCategoryResponseDTO.fromEntity(
                experienceCategoryService.update(categoryId, body)
        );
    }
}