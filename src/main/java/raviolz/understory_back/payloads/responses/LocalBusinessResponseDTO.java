package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.LocalBusiness;

import java.util.UUID;

public record LocalBusinessResponseDTO(
        UUID id,

        UUID cityId,
        String cityName,

        UUID businessCategoryId,
        String businessCategoryCode,
        String businessCategoryLabel,
        String businessCategoryIcon,

        String name,
        String address,
        String description,
        String websiteUrl,
        String imageUrl,

        double longitude,
        double latitude
) {
    public static LocalBusinessResponseDTO fromEntity(LocalBusiness business) {
        return new LocalBusinessResponseDTO(
                business.getId(),

                business.getCity().getId(),
                business.getCity().getName(),

                business.getBusinessCategory().getId(),
                business.getBusinessCategory().getCode(),
                business.getBusinessCategory().getLabel(),
                business.getBusinessCategory().getIcon(),

                business.getName(),
                business.getAddress(),
                business.getDescription(),
                business.getWebsiteUrl(),
                business.getImageUrl(),

                business.getLongitude(),
                business.getLatitude()
        );
    }
}