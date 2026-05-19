package raviolz.understory_back.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.entities.UserUploadSubmission;
import raviolz.understory_back.enums.GameType;
import raviolz.understory_back.enums.UploadSubmissionStatus;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.UserUploadSubmissionDTO;
import raviolz.understory_back.payloads.responses.UploadReviewResponseDTO;
import raviolz.understory_back.repositories.UserUploadSubmissionRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserUploadSubmissionService {

    private final UserUploadSubmissionRepository userUploadSubmissionRepository;
    private final UserService userService;
    private final ExperienceService experienceService;
    private final UserExperienceProgressService userExperienceProgressService;

    public UserUploadSubmissionService(
            UserUploadSubmissionRepository userUploadSubmissionRepository,
            UserService userService,
            ExperienceService experienceService,
            UserExperienceProgressService userExperienceProgressService
    ) {
        this.userUploadSubmissionRepository = userUploadSubmissionRepository;
        this.userService = userService;
        this.experienceService = experienceService;
        this.userExperienceProgressService = userExperienceProgressService;
    }

    public UserUploadSubmission submit(UserUploadSubmissionDTO body) {
        User user = userService.findById(body.userId());
        Experience experience = experienceService.findById(body.experienceId());

        if (experience.getGameType() != GameType.IMAGE_UPLOAD) {
            throw new ValidationException("Experience " + body.experienceId() + " is not an upload experience");
        }

        Optional<UserUploadSubmission> existingSubmission =
                userUploadSubmissionRepository.findByUserIdAndExperienceId(body.userId(), body.experienceId());

        if (existingSubmission.isPresent()) {
            UserUploadSubmission found = existingSubmission.get();

            if (found.getStatus() == UploadSubmissionStatus.APPROVED) {
                throw new ValidationException("Upload submission has already been approved");
            }

            found.resubmitImage(body.imageUrl());
            return userUploadSubmissionRepository.save(found);
        }

        UserUploadSubmission submission = new UserUploadSubmission(
                user,
                experience,
                body.imageUrl()
        );

        return userUploadSubmissionRepository.save(submission);
    }

    public Page<UserUploadSubmission> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userUploadSubmissionRepository.findAll(pageable);
    }

    public UserUploadSubmission findById(UUID id) {
        return userUploadSubmissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User upload submission with id " + id + " not found"));
    }

    public UserUploadSubmission findByUserAndExperience(UUID userId, UUID experienceId) {
        return userUploadSubmissionRepository.findByUserIdAndExperienceId(userId, experienceId)
                .orElseThrow(() -> new NotFoundException(
                        "Upload submission for user " + userId + " and experience " + experienceId + " not found"
                ));
    }

    public Page<UserUploadSubmission> findByUser(UUID userId, int page, int size, String sortBy) {
        userService.findById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userUploadSubmissionRepository.findByUserId(userId, pageable);
    }

    public Page<UserUploadSubmission> findByExperience(UUID experienceId, int page, int size, String sortBy) {
        experienceService.findById(experienceId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userUploadSubmissionRepository.findByExperienceId(experienceId, pageable);
    }

    public Page<UserUploadSubmission> findByStatus(UploadSubmissionStatus status, int page, int size, String sortBy) {
        if (status == null) {
            throw new ValidationException("Upload submission status is required");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userUploadSubmissionRepository.findByStatus(status, pageable);
    }

    @Transactional
    public UploadReviewResponseDTO approve(UUID id) {
        UserUploadSubmission found = findById(id);

        found.approve();
        userUploadSubmissionRepository.save(found);

        int xpGained = userExperienceProgressService.completeAndAwardXp(
                found.getUser().getId(),
                found.getExperience().getId()
        );

        String message = xpGained > 0
                ? "Upload approved. Experience completed."
                : "Upload approved. Experience was already completed.";

        return new UploadReviewResponseDTO(
                found.getId(),
                found.getUser().getId(),
                found.getExperience().getId(),
                found.getStatus(),
                true,
                xpGained,
                message
        );
    }

    public UploadReviewResponseDTO reject(UUID id) {
        UserUploadSubmission found = findById(id);

        found.reject();
        UserUploadSubmission saved = userUploadSubmissionRepository.save(found);

        return new UploadReviewResponseDTO(
                saved.getId(),
                saved.getUser().getId(),
                saved.getExperience().getId(),
                saved.getStatus(),
                false,
                0,
                "Upload rejected. User can submit a new image."
        );
    }


}