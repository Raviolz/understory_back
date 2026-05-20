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
import raviolz.understory_back.payloads.UserRewardDTO;
import raviolz.understory_back.repositories.RewardRepository;
import raviolz.understory_back.repositories.UserRewardRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserRewardService {

    private final UserRewardRepository userRewardRepository;
    private final UserService userService;
    private final RewardService rewardService;
    private final ExperienceService experienceService;
    private final RewardRepository rewardRepository;

    public UserRewardService(
            UserRewardRepository userRewardRepository,
            UserService userService,
            RewardService rewardService,
            ExperienceService experienceService,
            RewardRepository rewardRepository

    ) {
        this.userRewardRepository = userRewardRepository;
        this.userService = userService;
        this.rewardService = rewardService;
        this.experienceService = experienceService;
        this.rewardRepository = rewardRepository;
    }

    public UserReward unlock(UserRewardDTO body) {
        User user = userService.findById(body.userId());
        Reward reward = rewardService.findById(body.rewardId());

        if (!reward.isCurrentlyValid()) {
            throw new ValidationException("Reward " + body.rewardId() + " is not currently valid");
        }

        if (userRewardRepository.existsByUserIdAndRewardId(body.userId(), body.rewardId())) { // un utente puo' sbloccare lo stesso reward solo una volta
            throw new ValidationException("User " + body.userId() + " has already unlocked reward " + body.rewardId());
        }

        UserReward userReward = new UserReward(user, reward);

        return userRewardRepository.save(userReward);
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

    public Optional<UserReward> unlockRandomRewardForExperienceCity(UUID userId, UUID experienceId) {
        User user = userService.findById(userId);
        Experience experience = experienceService.findById(experienceId);

        UUID cityId = experience.getPointOfInterest().getCity().getId();

        List<Reward> availableRewards = rewardRepository.findByCityIdAndActiveTrue(cityId)
                .stream()
                .filter(Reward::isCurrentlyValid)
                .filter(reward -> !userRewardRepository.existsByUserIdAndRewardId(userId, reward.getId()))
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