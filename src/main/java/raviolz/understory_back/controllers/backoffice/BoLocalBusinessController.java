package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.payloads.LocalBusinessDTO;
import raviolz.understory_back.payloads.UpdateLocalBusinessDTO;
import raviolz.understory_back.payloads.responses.BoLocalBusinessResponseDTO;
import raviolz.understory_back.services.LocalBusinessService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/local-businesses")
public class BoLocalBusinessController {

    private final LocalBusinessService localBusinessService;

    public BoLocalBusinessController(LocalBusinessService localBusinessService) {
        this.localBusinessService = localBusinessService;
    }

    @GetMapping
    public Page<BoLocalBusinessResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "name") String sortBy) {
        return localBusinessService.findAll(page, size, sortBy)
                .map(BoLocalBusinessResponseDTO::fromEntity);
    }


    @GetMapping("/{businessId}")
    public BoLocalBusinessResponseDTO findById(@PathVariable UUID businessId) {
        return BoLocalBusinessResponseDTO.fromEntity(
                localBusinessService.findById(businessId)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoLocalBusinessResponseDTO save(@RequestBody @Valid LocalBusinessDTO body) {
        return BoLocalBusinessResponseDTO.fromEntity(
                localBusinessService.save(body)
        );
    }

    @PutMapping("/{businessId}")
    public BoLocalBusinessResponseDTO update(@PathVariable UUID businessId,
                                             @RequestBody @Valid UpdateLocalBusinessDTO body) {
        return BoLocalBusinessResponseDTO.fromEntity(
                localBusinessService.update(businessId, body)
        );
    }

    @PatchMapping(value = "/{businessId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BoLocalBusinessResponseDTO updateImage(@PathVariable UUID businessId,
                                                  @RequestParam("file") MultipartFile file) {
        return BoLocalBusinessResponseDTO.fromEntity(
                localBusinessService.updateImage(businessId, file)
        );
    }

    @PatchMapping("/{businessId}/publish")
    public BoLocalBusinessResponseDTO publish(@PathVariable UUID businessId) {
        return BoLocalBusinessResponseDTO.fromEntity(
                localBusinessService.publish(businessId)
        );
    }

    @PatchMapping("/{businessId}/unpublish")
    public BoLocalBusinessResponseDTO unpublish(@PathVariable UUID businessId) {
        return BoLocalBusinessResponseDTO.fromEntity(
                localBusinessService.unpublish(businessId)
        );
    }

    @DeleteMapping("/{businessId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID businessId) {
        localBusinessService.delete(businessId);
    }

}