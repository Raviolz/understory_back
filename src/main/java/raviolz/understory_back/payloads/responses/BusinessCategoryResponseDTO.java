package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.BusinessCategory;

import java.util.UUID;

public record BusinessCategoryResponseDTO(
        UUID id,
        String code,
        String label,
        String description,
        String icon
) {
    public static BusinessCategoryResponseDTO fromEntity(BusinessCategory businessCategory) {
        return new BusinessCategoryResponseDTO(
                businessCategory.getId(),
                businessCategory.getCode(),
                businessCategory.getLabel(),
                businessCategory.getDescription(),
                businessCategory.getIcon()
        );
    }
}