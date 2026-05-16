package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.UploadGame;
import raviolz.understory_back.payloads.UpdateUploadGameDTO;
import raviolz.understory_back.payloads.UploadGameDTO;
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
    public Page<UploadGame> findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "promptText") String sortBy) {
        return uploadGameService.findAll(page, size, sortBy);
    }

    @GetMapping("/{uploadGameId}")
    public UploadGame findById(@PathVariable UUID uploadGameId) {
        return uploadGameService.findById(uploadGameId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadGame save(@RequestBody @Valid UploadGameDTO body) {
        return uploadGameService.save(body);
    }

    @PutMapping("/{uploadGameId}")
    public UploadGame update(@PathVariable UUID uploadGameId,
                             @RequestBody @Valid UpdateUploadGameDTO body) {
        return uploadGameService.update(uploadGameId, body);
    }
}