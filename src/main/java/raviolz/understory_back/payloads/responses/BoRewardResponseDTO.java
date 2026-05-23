package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.Reward;
import raviolz.understory_back.enums.RewardType;

import java.time.LocalDate;
import java.util.UUID;

public record BoRewardResponseDTO(
        UUID id,

        UUID businessId,
        String businessName,

        UUID cityId,
        String cityName,

        String title,
        String description,
        String discountCode,
        RewardType rewardType,

        LocalDate validFrom,
        LocalDate validUntil,
        boolean active
) {
    public static BoRewardResponseDTO fromEntity(Reward reward) {
        return new BoRewardResponseDTO(
                reward.getId(),

                reward.getBusiness().getId(),
                reward.getBusiness().getName(),

                reward.getCity().getId(),
                reward.getCity().getName(),

                reward.getTitle(),
                reward.getDescription(),
                reward.getDiscountCode(),
                reward.getRewardType(),

                reward.getValidFrom(),
                reward.getValidUntil(),
                reward.isActive()
        );
    }
}