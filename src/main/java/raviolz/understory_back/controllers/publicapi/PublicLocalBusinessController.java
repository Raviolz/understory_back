package raviolz.understory_back.controllers.publicapi;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.payloads.responses.LocalBusinessResponseDTO;
import raviolz.understory_back.services.LocalBusinessService;

import java.util.UUID;

@RestController
@RequestMapping("/local-businesses")
public class PublicLocalBusinessController {

    private final LocalBusinessService localBusinessService;

    public PublicLocalBusinessController(LocalBusinessService localBusinessService) {
        this.localBusinessService = localBusinessService;
    }

    @GetMapping
    public Page<LocalBusinessResponseDTO> findAllActive(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "name") String sortBy) {
        return localBusinessService.findActive(page, size, sortBy)
                .map(LocalBusinessResponseDTO::fromEntity);
    }

    @GetMapping("/{businessId}")
    public LocalBusinessResponseDTO findActiveById(@PathVariable UUID businessId) {
        return LocalBusinessResponseDTO.fromEntity(
                localBusinessService.findActiveById(businessId)
        );
    }

    @GetMapping("/city/{cityId}")
    public Page<LocalBusinessResponseDTO> findActiveByCity(@PathVariable UUID cityId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "name") String sortBy) {
        return localBusinessService.findActiveByCity(cityId, page, size, sortBy)
                .map(LocalBusinessResponseDTO::fromEntity);
    }

    @GetMapping("/category/{categoryId}")
    public Page<LocalBusinessResponseDTO> findActiveByCategory(@PathVariable UUID categoryId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "name") String sortBy) {
        return localBusinessService.findActiveByBusinessCategory(categoryId, page, size, sortBy)
                .map(LocalBusinessResponseDTO::fromEntity);
    }
}