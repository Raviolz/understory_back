package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.BusinessCategory;
import raviolz.understory_back.payloads.BusinessCategoryDTO;
import raviolz.understory_back.payloads.UpdateBusinessCategoryDTO;
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
    public Page<BusinessCategory> findAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "label") String sortBy) {
        return businessCategoryService.findAll(page, size, sortBy);
    }

    @GetMapping("/{categoryId}")
    public BusinessCategory findById(@PathVariable UUID categoryId) {
        return businessCategoryService.findById(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusinessCategory save(@RequestBody @Valid BusinessCategoryDTO body) {
        return businessCategoryService.save(body);
    }

    @PutMapping("/{categoryId}")
    public BusinessCategory update(@PathVariable UUID categoryId,
                                   @RequestBody @Valid UpdateBusinessCategoryDTO body) {
        return businessCategoryService.update(categoryId, body);
    }
}