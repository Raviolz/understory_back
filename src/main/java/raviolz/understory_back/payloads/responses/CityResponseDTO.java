package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.City;
import raviolz.understory_back.entities.ExperienceCategory;

import java.util.Optional;
import java.util.UUID;

public record CityResponseDTO(
        UUID id,
        String name,
        String country,
        double longitude,
        double latitude,
        String description,
        String coverImageUrl,
        boolean active,
        String dominantCategoryCode,
        String dominantCategoryColor,
        String dominantCategoryIcon
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
                city.isActive(),
                null,
                null,
                null
        );
    }

    public static CityResponseDTO fromEntityWithDominantCategory(
            City city,
            Optional<ExperienceCategory> dominantCategory
    ) {
        return new CityResponseDTO(
                city.getId(),
                city.getName(),
                city.getCountry(),
                city.getLongitude(),
                city.getLatitude(),
                city.getDescription(),
                city.getCoverImageUrl(),
                city.isActive(),
                dominantCategory.map(ExperienceCategory::getCode).orElse(null),
                dominantCategory.map(ExperienceCategory::getColor).orElse(null),
                dominantCategory.map(ExperienceCategory::getIcon).orElse(null)
        );
    }
}