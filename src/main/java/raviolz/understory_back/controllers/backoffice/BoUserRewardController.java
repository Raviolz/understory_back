package raviolz.understory_back.controllers.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.enums.UserRewardStatus;
import raviolz.understory_back.payloads.responses.UserRewardResponseDTO;
import raviolz.understory_back.services.UserRewardService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/user-rewards")
public class BoUserRewardController {

    private final UserRewardService userRewardService;

    public BoUserRewardController(UserRewardService userRewardService) {
        this.userRewardService = userRewardService;
    }

    @GetMapping
    public Page<UserRewardResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "unlockedAt") String sortBy) {
        return userRewardService.findAll(page, size, sortBy)
                .map(UserRewardResponseDTO::fromEntity);
    }

    @GetMapping("/{userRewardId}")
    public UserRewardResponseDTO findById(@PathVariable UUID userRewardId) {
        return UserRewardResponseDTO.fromEntity(userRewardService.findById(userRewardId));
    }

    @GetMapping("/user/{userId}")
    public Page<UserRewardResponseDTO> findByUser(@PathVariable UUID userId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "unlockedAt") String sortBy) {
        return userRewardService.findByUser(userId, page, size, sortBy)
                .map(UserRewardResponseDTO::fromEntity);
    }

    @GetMapping("/reward/{rewardId}")
    public Page<UserRewardResponseDTO> findByReward(@PathVariable UUID rewardId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "unlockedAt") String sortBy) {
        return userRewardService.findByReward(rewardId, page, size, sortBy)
                .map(UserRewardResponseDTO::fromEntity);
    }

    @GetMapping("/status/{status}")
    public Page<UserRewardResponseDTO> findByStatus(@PathVariable UserRewardStatus status,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "unlockedAt") String sortBy) {
        return userRewardService.findByStatus(status, page, size, sortBy)
                .map(UserRewardResponseDTO::fromEntity);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public Page<UserRewardResponseDTO> findByUserAndStatus(@PathVariable UUID userId,
                                                           @PathVariable UserRewardStatus status,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "unlockedAt") String sortBy) {
        return userRewardService.findByUserAndStatus(userId, status, page, size, sortBy)
                .map(UserRewardResponseDTO::fromEntity);
    }

    @PatchMapping("/{userRewardId}/redeem")
    public UserRewardResponseDTO redeem(@PathVariable UUID userRewardId) {
        return UserRewardResponseDTO.fromEntity(userRewardService.redeem(userRewardId));
    }

    @PatchMapping("/{userRewardId}/expire")
    public UserRewardResponseDTO markAsExpired(@PathVariable UUID userRewardId) {
        return UserRewardResponseDTO.fromEntity(userRewardService.markAsExpired(userRewardId));
    }
}