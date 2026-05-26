package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.Booking;
import raviolz.understory_back.enums.BookingStatus;
import raviolz.understory_back.enums.RewardType;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingResponseDTO(
        UUID bookingId,
        UUID userRewardId,
        UUID userId,
        String username,

        UUID rewardId,
        String rewardTitle,
        String rewardDescription,
        String discountCode,
        RewardType rewardType,

        UUID businessId,
        String businessName,
        UUID cityId,
        String cityName,

        LocalDateTime bookingDate,
        BookingStatus status,
        String notes,
        int peopleCount
) {
    public static BookingResponseDTO fromEntity(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getUserReward().getId(),
                booking.getUserReward().getUser().getId(),
                booking.getUserReward().getUser().getUsername(),

                booking.getUserReward().getReward().getId(),
                booking.getUserReward().getReward().getTitle(),
                booking.getUserReward().getReward().getDescription(),
                booking.getUserReward().getReward().getDiscountCode(),
                booking.getUserReward().getReward().getRewardType(),

                booking.getUserReward().getReward().getBusiness().getId(),
                booking.getUserReward().getReward().getBusiness().getName(),
                booking.getUserReward().getReward().getCity().getId(),
                booking.getUserReward().getReward().getCity().getName(),

                booking.getBookingDate(),
                booking.getStatus(),
                booking.getNotes(),
                booking.getPeopleCount()
        );
    }
}