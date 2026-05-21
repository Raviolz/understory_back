package raviolz.understory_back.controllers.gameplay;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.entities.UserUploadSubmission;
import raviolz.understory_back.payloads.responses.UploadSubmissionResponseDTO;
import raviolz.understory_back.services.UserUploadSubmissionService;

import java.util.UUID;

@RestController
@RequestMapping("/gameplay/upload-submissions")
public class UploadGameplayController {

    private final UserUploadSubmissionService userUploadSubmissionService;

    public UploadGameplayController(UserUploadSubmissionService userUploadSubmissionService) {
        this.userUploadSubmissionService = userUploadSubmissionService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UploadSubmissionResponseDTO submit(@AuthenticationPrincipal User currentUser,
                                              @RequestParam("experienceId") UUID experienceId,
                                              @RequestParam("file") MultipartFile file) {
        UserUploadSubmission submission = userUploadSubmissionService.submit(currentUser.getId(), experienceId,
                file);
        return UploadSubmissionResponseDTO.fromEntity(submission);
    }
}