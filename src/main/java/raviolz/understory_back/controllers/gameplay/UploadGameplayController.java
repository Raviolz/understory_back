package raviolz.understory_back.controllers.gameplay;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.UserUploadSubmission;
import raviolz.understory_back.payloads.UserUploadSubmissionDTO;
import raviolz.understory_back.payloads.responses.UploadSubmissionResponseDTO;
import raviolz.understory_back.services.UserUploadSubmissionService;

@RestController
@RequestMapping("/gameplay/upload-submissions")
public class UploadGameplayController {

    private final UserUploadSubmissionService userUploadSubmissionService;

    public UploadGameplayController(UserUploadSubmissionService userUploadSubmissionService) {
        this.userUploadSubmissionService = userUploadSubmissionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadSubmissionResponseDTO submit(@RequestBody @Valid UserUploadSubmissionDTO body) {
        UserUploadSubmission submission = userUploadSubmissionService.submit(body);
        return UploadSubmissionResponseDTO.fromEntity(submission);
    }
}