package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.entities.UserExperienceProgress;
import raviolz.understory_back.enums.ProgressStatus;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.payloads.UserExperienceProgressDTO;
import raviolz.understory_back.payloads.UserNoteDTO;
import raviolz.understory_back.repositories.UserExperienceProgressRepository;

import java.util.UUID;

@Service
public class UserExperienceProgressService {

    private final UserExperienceProgressRepository userExperienceProgressRepository;
    private final UserService userService;
    private final ExperienceService experienceService;

    public UserExperienceProgressService(UserExperienceProgressRepository userExperienceProgressRepository, UserService userService, ExperienceService experienceService

    ) {
        this.userExperienceProgressRepository = userExperienceProgressRepository;
        this.userService = userService;
        this.experienceService = experienceService;
    }

    public UserExperienceProgress startOrGet(UserExperienceProgressDTO body) {
        User user = userService.findById(body.userId());
        Experience experience = experienceService.findById(body.experienceId());

        return userExperienceProgressRepository
                .findByUserIdAndExperienceId(body.userId(), body.experienceId())
                .orElseGet(() -> userExperienceProgressRepository.save(
                        new UserExperienceProgress(user, experience)
                ));
    }

    public Page<UserExperienceProgress> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userExperienceProgressRepository.findAll(pageable);
    }

    public UserExperienceProgress findById(UUID id) {
        return userExperienceProgressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User experience progress with id " + id + " not found"));
    }

    public UserExperienceProgress findByUserAndExperience(UUID userId, UUID experienceId) {
        return userExperienceProgressRepository.findByUserIdAndExperienceId(userId, experienceId)
                .orElseThrow(() -> new NotFoundException(
                        "Progress for user " + userId + " and experience " + experienceId + " not found"
                ));
    }

    public Page<UserExperienceProgress> findByUser(UUID userId, int page, int size, String sortBy) {
        userService.findById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userExperienceProgressRepository.findByUserId(userId, pageable);
    }

    public Page<UserExperienceProgress> findCompletedByUser(UUID userId, int page, int size, String sortBy) {
        userService.findById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userExperienceProgressRepository.findByUserIdAndStatus(userId, ProgressStatus.COMPLETED, pageable);
    }

    public UserExperienceProgress updateUserNote(UUID progressId, UserNoteDTO body) {
        UserExperienceProgress found = findById(progressId);

        found.updateUserNote(body.userNote());

        return userExperienceProgressRepository.save(found);
    }
}
