package raviolz.understory_back.payloads.responses;

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
        boolean active
) {
    public static PointOfInterestResponseDTO fromEntity(PointOfInterest pointOfInterest) {
        return new PointOfInterestResponseDTO(
                pointOfInterest.getId(),
                pointOfInterest.getCity().getId(),
                pointOfInterest.getCity().getName(),
                pointOfInterest.getName(),
                pointOfInterest.getShortDescription(),
                pointOfInterest.getImageUrl(),
                pointOfInterest.getLongitude(),
                pointOfInterest.getLatitude(),
                pointOfInterest.isActive()
        );
    }
}