package raviolz.understory_back.payloads.responses;

import java.util.UUID;

public record CityKnowledgeResponseDTO(
        UUID cityId,
        String cityName,
        long completedExperiences,
        long totalExperiences,
        int percentage
) {
}