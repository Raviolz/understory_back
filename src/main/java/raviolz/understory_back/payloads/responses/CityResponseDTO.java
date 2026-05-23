package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.City;

import java.util.UUID;

public record CityResponseDTO(
        UUID id,
        String name,
        String country,
        double longitude,
        double latitude,
        String description,
        String coverImageUrl,
        boolean active
) {
    public static CityResponseDTO fromEntity(City city) {
        return new CityResponseDTO(
                city.getId(),
                city.getName(),
                city.getCountry(),
                city.getLongitude(),
                city.getLatitude(),
                city.getDescription(),
                city.getCoverImageUrl(),
                city.isActive()
        );
    }
}