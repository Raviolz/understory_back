package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.Reward;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.entities.UserReward;
import raviolz.understory_back.enums.UserRewardStatus;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.repositories.RewardRepository;
import raviolz.understory_back.repositories.UserRewardRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserRewardService {

    private static final int COMPLETED_EXPERIENCES_PER_REWARD = 3;

    private final UserRewardRepository userRewardRepository;
    private final UserService userService;
    private final RewardService rewardService;
    private final ExperienceService experienceService;
    private final RewardRepository rewardRepository;
    private final UserExperienceProgressService userExperienceProgressService;

    public UserRewardService(
            UserRewardRepository userRewardRepository,
            UserService userService,
            RewardService rewardService,
            ExperienceService experienceService,
            RewardRepository rewardRepository,
            UserExperienceProgressService userExperienceProgressService

    ) {
        this.userRewardRepository = userRewardRepository;
        this.userService = userService;
        this.rewardService = rewardService;
        this.experienceService = experienceService;
        this.rewardRepository = rewardRepository;
        this.userExperienceProgressService = userExperienceProgressService;
    }

    public Page<UserReward> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRewardRepository.findAll(pageable);
    }

    public UserReward findById(UUID id) {
        return userRewardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User reward with id " + id + " not found"));
    }

    public UserReward findByUserAndReward(UUID userId, UUID rewardId) {
        return userRewardRepository.findByUserIdAndRewardId(userId, rewardId)
                .orElseThrow(() -> new NotFoundException(
                        "User reward for user " + userId + " and reward " + rewardId + " not found"
                ));
    }

    public Page<UserReward> findByUser(UUID userId, int page, int size, String sortBy) {
        userService.findById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRewardRepository.findByUserId(userId, pageable);
    }

    public Page<UserReward> findByReward(UUID rewardId, int page, int size, String sortBy) {
        rewardService.findById(rewardId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRewardRepository.findByRewardId(rewardId, pageable);
    }

    public Page<UserReward> findByStatus(UserRewardStatus status, int page, int size, String sortBy) {
        if (status == null) {
            throw new ValidationException("User reward status is required");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRewardRepository.findByStatus(status, pageable);
    }

    public Page<UserReward> findByUserAndStatus(UUID userId, UserRewardStatus status, int page, int size, String sortBy) {
        userService.findById(userId);

        if (status == null) {
            throw new ValidationException("User reward status is required");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRewardRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    public UserReward redeem(UUID id) {
        UserReward found = findById(id);
        found.redeem();
        return userRewardRepository.save(found);
    }

    public UserReward markAsExpired(UUID id) {
        UserReward found = findById(id);
        found.markAsExpired();
        return userRewardRepository.save(found);
    }

    public UserReward expireIfRewardExpired(UUID userRewardId) {
        UserReward found = findById(userRewardId);

        if (found.getStatus() == UserRewardStatus.REDEEMED) {
            return found;
        }

        if (found.getReward().isExpired()) {
            found.markAsExpired();
            return userRewardRepository.save(found);
        }

        return found;
    }

    public Optional<UserReward> unlockRandomRewardForExperienceCityIfMilestoneReached(UUID userId, UUID experienceId) {
        User user = userService.findById(userId);
        Experience experience = experienceService.findById(experienceId);

        UUID cityId = experience.getPointOfInterest().getCity().getId();

        long completedCount = userExperienceProgressService.countCompletedByUserAndCity(userId, cityId);

        if (completedCount == 0 || completedCount % COMPLETED_EXPERIENCES_PER_REWARD != 0) {
            return Optional.empty();
        }

        return unlockRandomRewardForCity(user, cityId);
    }

    private Optional<UserReward> unlockRandomRewardForCity(User user, UUID cityId) {
        List<Reward> availableRewards = rewardRepository.findByCityIdAndActiveTrue(cityId)
                .stream()
                .filter(Reward::isCurrentlyValid)
                .filter(reward -> !userRewardRepository.existsByUserIdAndRewardId(user.getId(), reward.getId()))
                .toList();

        if (availableRewards.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(availableRewards.size());
        Reward selectedReward = availableRewards.get(randomIndex);

        UserReward userReward = new UserReward(user, selectedReward);

        return Optional.of(userRewardRepository.save(userReward));
    }
}