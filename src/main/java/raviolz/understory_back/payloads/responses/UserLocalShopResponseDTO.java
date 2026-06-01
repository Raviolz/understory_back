package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.LocalBusiness;
import raviolz.understory_back.entities.UserReward;
import raviolz.understory_back.enums.UserRewardStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserLocalShopResponseDTO(
        UUID businessId,
        String businessName,
        String businessCategory,
        String cityName,
        String address,
        String description,
        String websiteUrl,
        String imageUrl,
        double longitude,
        double latitude,

        UUID rewardId,
        String rewardTitle,
        String rewardDescription,
        String rewardType,
        UserRewardStatus rewardStatus,
        LocalDateTime unlockedAt
) {
    public static UserLocalShopResponseDTO fromEntity(UserReward userReward) {
        LocalBusiness business = userReward.getReward().getBusiness();

        return new UserLocalShopResponseDTO(
                business.getId(),
                business.getName(),
                business.getBusinessCategory().getCode(),
                business.getCity().getName(),
                business.getAddress(),
                business.getDescription(),
                business.getWebsiteUrl(),
                business.getImageUrl(),
                business.getLongitude(),
                business.getLatitude(),

                userReward.getReward().getId(),
                userReward.getReward().getTitle(),
                userReward.getReward().getDescription(),
                userReward.getReward().getRewardType().name(),
                userReward.getStatus(),
                userReward.getUnlockedAt()
        );
    }
}