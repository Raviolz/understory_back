package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.UserReward;
import raviolz.understory_back.enums.RewardType;
import raviolz.understory_back.enums.UserRewardStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRewardResponseDTO(
        UUID userRewardId,
        UUID userId,
        String username,
        UUID rewardId,
        String rewardTitle,
        String rewardDescription,
        String discountCode,
        RewardType rewardType,
        UUID cityId,
        String cityName,
        UUID businessId,
        String businessName,
        UserRewardStatus status,
        LocalDateTime unlockedAt,
        LocalDateTime redeemedAt
) {
    public static UserRewardResponseDTO fromEntity(UserReward userReward) {
        return new UserRewardResponseDTO(
                userReward.getId(),
                userReward.getUser().getId(),
                userReward.getUser().getUsername(),
                userReward.getReward().getId(),
                userReward.getReward().getTitle(),
                userReward.getReward().getDescription(),
                userReward.getReward().getDiscountCode(),
                userReward.getReward().getRewardType(),
                userReward.getReward().getCity().getId(),
                userReward.getReward().getCity().getName(),
                userReward.getReward().getBusiness().getId(),
                userReward.getReward().getBusiness().getName(),
                userReward.getStatus(),
                userReward.getUnlockedAt(),
                userReward.getRedeemedAt()
        );
    }
}