package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.LocalBusiness;

import java.util.UUID;

public record BoLocalBusinessResponseDTO(
        UUID id,

        UUID cityId,
        String cityName,

        UUID businessCategoryId,
        String businessCategoryLabel,
        String businessCategoryCode,

        String name,
        String address,
        String description,
        String websiteUrl,
        String imageUrl,

        double longitude,
        double latitude,
        boolean active
) {
    public static BoLocalBusinessResponseDTO fromEntity(LocalBusiness business) {
        return new BoLocalBusinessResponseDTO(
                business.getId(),

                business.getCity().getId(),
                business.getCity().getName(),

                business.getBusinessCategory().getId(),
                business.getBusinessCategory().getLabel(),
                business.getBusinessCategory().getCode(),

                business.getName(),
                business.getAddress(),
                business.getDescription(),
                business.getWebsiteUrl(),
                business.getImageUrl(),

                business.getLongitude(),
                business.getLatitude(),
                business.isActive()
        );
    }
}