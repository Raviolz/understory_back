package raviolz.understory_back.controllers.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.enums.UploadSubmissionStatus;
import raviolz.understory_back.payloads.responses.BoUploadSubmissionResponseDTO;
import raviolz.understory_back.payloads.responses.UploadReviewResponseDTO;
import raviolz.understory_back.services.UserUploadSubmissionService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/upload-submissions")
public class BoUploadSubmissionController {

    private final UserUploadSubmissionService userUploadSubmissionService;

    public BoUploadSubmissionController(UserUploadSubmissionService userUploadSubmissionService) {
        this.userUploadSubmissionService = userUploadSubmissionService;
    }

    @GetMapping
    public Page<BoUploadSubmissionResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "submittedAt") String sortBy) {
        return userUploadSubmissionService.findAll(page, size, sortBy)
                .map(BoUploadSubmissionResponseDTO::fromEntity);
    }

    @GetMapping("/{submissionId}")
    public BoUploadSubmissionResponseDTO findById(@PathVariable UUID submissionId) {
        return BoUploadSubmissionResponseDTO.fromEntity(
                userUploadSubmissionService.findById(submissionId)
        );
    }

    @GetMapping("/status/{status}")
    public Page<BoUploadSubmissionResponseDTO> findByStatus(@PathVariable UploadSubmissionStatus status,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "submittedAt") String sortBy) {
        return userUploadSubmissionService.findByStatus(status, page, size, sortBy)
                .map(BoUploadSubmissionResponseDTO::fromEntity);
    }

    @PatchMapping("/{submissionId}/approve")
    public UploadReviewResponseDTO approve(@PathVariable UUID submissionId) {
        return userUploadSubmissionService.approve(submissionId);
    }

    @PatchMapping("/{submissionId}/reject")
    public UploadReviewResponseDTO reject(@PathVariable UUID submissionId) {
        return userUploadSubmissionService.reject(submissionId);
    }
}