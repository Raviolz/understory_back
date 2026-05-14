package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.ExperienceCategory;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.ExperienceCategoryDTO;
import raviolz.understory_back.payloads.UpdateExperienceCategoryDTO;
import raviolz.understory_back.repositories.ExperienceCategoryRepository;

import java.util.UUID;

@Service
public class ExperienceCategoryService {

    private final ExperienceCategoryRepository experienceCategoryRepository;

    public ExperienceCategoryService(ExperienceCategoryRepository experienceCategoryRepository) {
        this.experienceCategoryRepository = experienceCategoryRepository;
    }

    public ExperienceCategory save(ExperienceCategoryDTO body) {
        String normalizedCode = body.code().trim().toUpperCase();
        String normalizedLabel = body.label().trim();

        if (experienceCategoryRepository.existsByCode(normalizedCode)) {
            throw new ValidationException("Experience category code " + body.code() + " already exists");
        }

        if (experienceCategoryRepository.existsByLabel(normalizedLabel)) {
            throw new ValidationException("Experience category label " + body.label() + " already exists");
        }

        ExperienceCategory experienceCategory = new ExperienceCategory(
                body.label(),
                body.code(),
                body.description(),
                body.icon(),
                body.color()
        );

        return experienceCategoryRepository.save(experienceCategory);
    }

    public Page<ExperienceCategory> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return experienceCategoryRepository.findAll(pageable);
    }

    public ExperienceCategory findById(UUID id) {
        return experienceCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Experience category with id " + id + " not found"));
    }

    public ExperienceCategory findByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Experience category code is required");
        }

        String normalizedCode = code.trim().toUpperCase();

        return experienceCategoryRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new NotFoundException("Experience category with code " + code + " not found"));
    }

    public ExperienceCategory update(UUID id, UpdateExperienceCategoryDTO body) {
        ExperienceCategory found = findById(id);

        String normalizedLabel = body.label().trim();

        if (!found.getLabel().equals(normalizedLabel) &&
                experienceCategoryRepository.existsByLabel(normalizedLabel)) {
            throw new ValidationException("Experience category label " + body.label() + " already exists");
        }

        found.setLabel(body.label());
        found.setDescription(body.description());
        found.setIcon(body.icon());
        found.setColor(body.color());

        return experienceCategoryRepository.save(found);
    }
}