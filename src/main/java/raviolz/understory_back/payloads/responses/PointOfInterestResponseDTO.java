package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.PointOfInterest;

import java.util.UUID;

public record PointOfInterestResponseDTO(
        UUID id,
        UUID cityId,
        String cityName,
        String name,
        String shortDescription,
        String imageUrl,
        double longitude,
        double latitude,
        boolean active,
        UUID primaryExperienceId,
        String primaryExperienceTitle,
        String primaryExperienceCategoryCode,
        String primaryExperienceCategoryColor,
        String primaryExperienceCategoryIcon
) {
    public static PointOfInterestResponseDTO fromEntity(PointOfInterest pointOfInterest) {
        return fromEntity(pointOfInterest, null);
    }

    public static PointOfInterestResponseDTO fromEntity(PointOfInterest pointOfInterest, Experience primaryExperience) {
        return new PointOfInterestResponseDTO(
                pointOfInterest.getId(),
                pointOfInterest.getCity().getId(),
                pointOfInterest.getCity().getName(),
                pointOfInterest.getName(),
                pointOfInterest.getShortDescription(),
                pointOfInterest.getImageUrl(),
                pointOfInterest.getLongitude(),
                pointOfInterest.getLatitude(),
                pointOfInterest.isActive(),
                primaryExperience != null ? primaryExperience.getId() : null,
                primaryExperience != null ? primaryExperience.getTitle() : null,
                primaryExperience != null ? primaryExperience.getExperienceCategory().getCode() : null,
                primaryExperience != null ? primaryExperience.getExperienceCategory().getColor() : null,
                primaryExperience != null ? primaryExperience.getExperienceCategory().getIcon() : null
        );
    }
}