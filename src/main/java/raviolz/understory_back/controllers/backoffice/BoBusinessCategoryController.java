package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.payloads.BusinessCategoryDTO;
import raviolz.understory_back.payloads.UpdateBusinessCategoryDTO;
import raviolz.understory_back.payloads.responses.BusinessCategoryResponseDTO;
import raviolz.understory_back.services.BusinessCategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/business-categories")
public class BoBusinessCategoryController {

    private final BusinessCategoryService businessCategoryService;

    public BoBusinessCategoryController(BusinessCategoryService businessCategoryService) {
        this.businessCategoryService = businessCategoryService;
    }

    @GetMapping
    public Page<BusinessCategoryResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "label") String sortBy) {
        return businessCategoryService.findAll(page, size, sortBy)
                .map(BusinessCategoryResponseDTO::fromEntity);
    }

    @GetMapping("/{categoryId}")
    public BusinessCategoryResponseDTO findById(@PathVariable UUID categoryId) {
        return BusinessCategoryResponseDTO.fromEntity(
                businessCategoryService.findById(categoryId)
        );
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessCategoryResponseDTO save(@RequestBody @Valid BusinessCategoryDTO body) {
        return BusinessCategoryResponseDTO.fromEntity(
                businessCategoryService.save(body)
        );
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping("/{categoryId}")
    public BusinessCategoryResponseDTO update(@PathVariable UUID categoryId,
                                              @RequestBody @Valid UpdateBusinessCategoryDTO body) {
        return BusinessCategoryResponseDTO.fromEntity(
                businessCategoryService.update(categoryId, body)
        );
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID categoryId) {
        businessCategoryService.delete(categoryId);
    }
}