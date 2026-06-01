package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.payloads.UpdateUploadGameDTO;
import raviolz.understory_back.payloads.UploadGameDTO;
import raviolz.understory_back.payloads.responses.BoUploadGameResponseDTO;
import raviolz.understory_back.services.UploadGameService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/upload-games")
public class BoUploadGameController {

    private final UploadGameService uploadGameService;

    public BoUploadGameController(UploadGameService uploadGameService) {
        this.uploadGameService = uploadGameService;
    }

    @GetMapping
    public Page<BoUploadGameResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "promptText") String sortBy) {
        return uploadGameService.findAll(page, size, sortBy)
                .map(BoUploadGameResponseDTO::fromEntity);
    }

    @GetMapping("/{uploadGameId}")
    public BoUploadGameResponseDTO findById(@PathVariable UUID uploadGameId) {
        return BoUploadGameResponseDTO.fromEntity(
                uploadGameService.findById(uploadGameId)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoUploadGameResponseDTO save(@RequestBody @Valid UploadGameDTO body) {
        return BoUploadGameResponseDTO.fromEntity(
                uploadGameService.save(body)
        );
    }

    @PutMapping("/{uploadGameId}")
    public BoUploadGameResponseDTO update(@PathVariable UUID uploadGameId,
                                          @RequestBody @Valid UpdateUploadGameDTO body) {
        return BoUploadGameResponseDTO.fromEntity(
                uploadGameService.update(uploadGameId, body)
        );
    }

    @PatchMapping(value = "/{uploadGameId}/reference-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BoUploadGameResponseDTO updateReferenceImage(@PathVariable UUID uploadGameId,
                                                        @RequestParam("file") MultipartFile file) {
        return BoUploadGameResponseDTO.fromEntity(
                uploadGameService.updateReferenceImage(uploadGameId, file)
        );
    }

    @DeleteMapping("/{uploadGameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID uploadGameId) {
        uploadGameService.delete(uploadGameId);
    }
}