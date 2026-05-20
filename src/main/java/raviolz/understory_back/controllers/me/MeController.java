package raviolz.understory_back.controllers.me;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.payloads.UpdateUserProfileDTO;
import raviolz.understory_back.payloads.UserNoteDTO;
import raviolz.understory_back.payloads.responses.UserJournalEntryResponseDTO;
import raviolz.understory_back.payloads.responses.UserProgressResponseDTO;
import raviolz.understory_back.payloads.responses.UserResponseDTO;
import raviolz.understory_back.payloads.responses.UserRewardResponseDTO;
import raviolz.understory_back.services.UserExperienceProgressService;
import raviolz.understory_back.services.UserRewardService;
import raviolz.understory_back.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/me")
public class MeController {

    private final UserRewardService userRewardService;
    private final UserExperienceProgressService userExperienceProgressService;
    private final UserService userService;

    public MeController(UserRewardService userRewardService, UserExperienceProgressService userExperienceProgressService, UserService userService) {
        this.userRewardService = userRewardService;
        this.userExperienceProgressService = userExperienceProgressService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserResponseDTO getProfile(@AuthenticationPrincipal User currentUser) {
        return UserResponseDTO.fromEntity(currentUser);
    }

    @PatchMapping("/profile")
    public UserResponseDTO updateProfile(@AuthenticationPrincipal User currentUser,
                                         @RequestBody @Valid UpdateUserProfileDTO body) {
        return UserResponseDTO.fromEntity(
                userService.updateProfile(currentUser.getId(), body)
        );
    }

    @GetMapping("/rewards")
    public Page<UserRewardResponseDTO> getMyRewards(@AuthenticationPrincipal User currentUser,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "unlockedAt") String sortBy) {
        return userRewardService.findByUser(currentUser.getId(), page, size, sortBy)
                .map(UserRewardResponseDTO::fromEntity);
    }

    @GetMapping("/progress")
    public Page<UserProgressResponseDTO> getMyProgress(@AuthenticationPrincipal User currentUser,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "startedAt") String sortBy) {
        return userExperienceProgressService.findByUser(currentUser.getId(), page, size, sortBy)
                .map(UserProgressResponseDTO::fromEntity);
    }

    @GetMapping("/journal")
    public Page<UserJournalEntryResponseDTO> getMyJournal(@AuthenticationPrincipal User currentUser,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "completedAt") String sortBy) {
        return userExperienceProgressService.findCompletedByUser(currentUser.getId(), page, size, sortBy)
                .map(UserJournalEntryResponseDTO::fromEntity);
    }

    @PatchMapping("/progress/{progressId}/note")
    public UserProgressResponseDTO updateMyProgressNote(@AuthenticationPrincipal User currentUser,
                                                        @PathVariable UUID progressId,
                                                        @RequestBody @Valid UserNoteDTO body) {
        return UserProgressResponseDTO.fromEntity(
                userExperienceProgressService.updateUserNoteForUser(
                        currentUser.getId(),
                        progressId,
                        body
                )
        );
    }
}