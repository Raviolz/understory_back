package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.ExperienceCategory;

import java.util.UUID;

public record ExperienceCategoryResponseDTO(
        UUID id,
        String code,
        String label,
        String description,
        String icon,
        String color
) {
    public static ExperienceCategoryResponseDTO fromEntity(ExperienceCategory category) {
        return new ExperienceCategoryResponseDTO(
                category.getId(),
                category.getCode(),
                category.getLabel(),
                category.getDescription(),
                category.getIcon(),
                category.getColor()
        );
    }
}