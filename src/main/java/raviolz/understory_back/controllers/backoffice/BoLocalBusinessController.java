package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.entities.LocalBusiness;
import raviolz.understory_back.payloads.LocalBusinessDTO;
import raviolz.understory_back.payloads.UpdateLocalBusinessDTO;
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
    public Page<LocalBusiness> findAll(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "name") String sortBy) {
        return localBusinessService.findAll(page, size, sortBy);
    }

    @GetMapping("/{businessId}")
    public LocalBusiness findById(@PathVariable UUID businessId) {
        return localBusinessService.findById(businessId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocalBusiness save(@RequestBody @Valid LocalBusinessDTO body) {
        return localBusinessService.save(body);
    }

    @PutMapping("/{businessId}")
    public LocalBusiness update(@PathVariable UUID businessId,
                                @RequestBody @Valid UpdateLocalBusinessDTO body) {
        return localBusinessService.update(businessId, body);
    }

    @PatchMapping(value = "/{businessId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public LocalBusiness updateImage(@PathVariable UUID businessId,
                                     @RequestParam("file") MultipartFile file) {
        return localBusinessService.updateImage(businessId, file);
    }

    @PatchMapping("/{businessId}/publish")
    public LocalBusiness publish(@PathVariable UUID businessId) {
        return localBusinessService.publish(businessId);
    }

    @PatchMapping("/{businessId}/unpublish")
    public LocalBusiness unpublish(@PathVariable UUID businessId) {
        return localBusinessService.unpublish(businessId);
    }
}