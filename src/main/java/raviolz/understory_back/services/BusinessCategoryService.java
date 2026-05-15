package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.BusinessCategory;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.BusinessCategoryDTO;
import raviolz.understory_back.payloads.UpdateBusinessCategoryDTO;
import raviolz.understory_back.repositories.BusinessCategoryRepository;

import java.util.UUID;

@Service
public class BusinessCategoryService {

    private final BusinessCategoryRepository businessCategoryRepository;

    public BusinessCategoryService(BusinessCategoryRepository businessCategoryRepository) {
        this.businessCategoryRepository = businessCategoryRepository;
    }

    public BusinessCategory save(BusinessCategoryDTO body) {
        String normalizedCode = body.code().trim().toUpperCase();
        String normalizedLabel = body.label().trim();

        if (businessCategoryRepository.existsByCode(normalizedCode)) {
            throw new ValidationException("Business category code " + body.code() + " already exists");
        }

        if (businessCategoryRepository.existsByLabel(normalizedLabel)) {
            throw new ValidationException("Business category label " + body.label() + " already exists");
        }

        BusinessCategory businessCategory = new BusinessCategory(
                body.code(),
                body.label(),
                body.description(),
                body.icon()
        );

        return businessCategoryRepository.save(businessCategory);
    }

    public Page<BusinessCategory> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return businessCategoryRepository.findAll(pageable);
    }

    public BusinessCategory findById(UUID id) {
        return businessCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Business category with id " + id + " not found"));
    }

    public BusinessCategory findByCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Business category code is required");
        }

        String normalizedCode = code.trim().toUpperCase();

        return businessCategoryRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new NotFoundException("Business category with code " + code + " not found"));
    }

    public BusinessCategory update(UUID id, UpdateBusinessCategoryDTO body) {
        BusinessCategory found = findById(id);

        String normalizedLabel = body.label().trim();

        if (!found.getLabel().equals(normalizedLabel) &&
                businessCategoryRepository.existsByLabel(normalizedLabel)) {
            throw new ValidationException("Business category label " + body.label() + " already exists");
        }

        found.setLabel(body.label());
        found.setDescription(body.description());
        found.setIcon(body.icon());

        return businessCategoryRepository.save(found);
    }
}