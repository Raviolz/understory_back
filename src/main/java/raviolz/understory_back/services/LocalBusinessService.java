package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.entities.BusinessCategory;
import raviolz.understory_back.entities.City;
import raviolz.understory_back.entities.LocalBusiness;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.LocalBusinessDTO;
import raviolz.understory_back.payloads.UpdateLocalBusinessDTO;
import raviolz.understory_back.repositories.LocalBusinessRepository;
import raviolz.understory_back.repositories.RewardRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class LocalBusinessService {

    private final LocalBusinessRepository localBusinessRepository;
    private final CityService cityService;
    private final BusinessCategoryService businessCategoryService;
    private final ImageUploadService imageUploadService;
    private final RewardRepository rewardRepository;

    public LocalBusinessService(
            LocalBusinessRepository localBusinessRepository,
            CityService cityService,
            BusinessCategoryService businessCategoryService,
            ImageUploadService imageUploadService,
            RewardRepository rewardRepository
    ) {
        this.localBusinessRepository = localBusinessRepository;
        this.cityService = cityService;
        this.businessCategoryService = businessCategoryService;
        this.imageUploadService = imageUploadService;
        this.rewardRepository = rewardRepository;
    }

    public LocalBusiness save(LocalBusinessDTO body) {
        City city = cityService.findById(body.cityId());
        BusinessCategory businessCategory = businessCategoryService.findById(body.businessCategoryId());

        String normalizedName = body.name().trim();
        String normalizedAddress = body.address().trim();

        if (localBusinessRepository.existsByNameAndAddressAndCityId(normalizedName, normalizedAddress, body.cityId())) {
            throw new ValidationException("Local business " + body.name() + " at address " + body.address() + " already exists in this city");
        }

        LocalBusiness localBusiness = new LocalBusiness(
                city,
                businessCategory,
                body.name(),
                body.address(),
                body.description(),
                body.websiteUrl(),
                body.longitude(),
                body.latitude()
        );

        return localBusinessRepository.save(localBusiness);
    }

    public Page<LocalBusiness> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return localBusinessRepository.findAll(pageable);
    }

    public Page<LocalBusiness> findActive(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return localBusinessRepository.findByActiveTrue(pageable);
    }

    public LocalBusiness findById(UUID id) {
        return localBusinessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Local business with id " + id + " not found"));
    }

    public Page<LocalBusiness> findByCity(UUID cityId, int page, int size, String sortBy) {
        cityService.findById(cityId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return localBusinessRepository.findByCityId(cityId, pageable);
    }

    public Page<LocalBusiness> findByBusinessCategory(UUID businessCategoryId, int page, int size, String sortBy) {
        businessCategoryService.findById(businessCategoryId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return localBusinessRepository.findByBusinessCategoryId(businessCategoryId, pageable);
    }

    public LocalBusiness findActiveById(UUID id) {
        return localBusinessRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Local business with id " + id + " not found"));
    }

    public Page<LocalBusiness> findActiveByCity(UUID cityId, int page, int size, String sortBy) {
        cityService.findActiveById(cityId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return localBusinessRepository.findByCityIdAndActiveTrue(cityId, pageable);
    }

    public Page<LocalBusiness> findActiveByBusinessCategory(UUID businessCategoryId, int page, int size, String sortBy) {
        businessCategoryService.findById(businessCategoryId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return localBusinessRepository.findByBusinessCategoryIdAndActiveTrue(businessCategoryId, pageable);
    }

    public LocalBusiness update(UUID id, UpdateLocalBusinessDTO body) {
        LocalBusiness found = findById(id);

        City city = cityService.findById(body.cityId());
        BusinessCategory businessCategory = businessCategoryService.findById(body.businessCategoryId());

        String normalizedName = body.name().trim();
        String normalizedAddress = body.address().trim();

        Optional<LocalBusiness> existingBusiness =
                localBusinessRepository.findByNameAndAddressAndCityId(
                        normalizedName,
                        normalizedAddress,
                        body.cityId()
                );


        if (existingBusiness.isPresent() && !existingBusiness.get().getId().equals(found.getId())) {
            throw new ValidationException("Local business " + body.name() + " at address " + body.address() + " already exists in this city");
        }

        found.setCity(city);
        found.setBusinessCategory(businessCategory);
        found.setName(body.name());
        found.setAddress(body.address());
        found.setDescription(body.description());
        found.setWebsiteUrl(body.websiteUrl());
        found.setLongitude(body.longitude());
        found.setLatitude(body.latitude());

        return localBusinessRepository.save(found);
    }


    public LocalBusiness updateImage(UUID businessId, MultipartFile file) {
        LocalBusiness found = findById(businessId);

        String imageUrl = imageUploadService.uploadImage(file);

        found.setImageUrl(imageUrl);

        return localBusinessRepository.save(found);
    }


    public LocalBusiness publish(UUID id) {
        LocalBusiness found = findById(id);
        found.publish();
        return localBusinessRepository.save(found);
    }

    public LocalBusiness unpublish(UUID id) {
        LocalBusiness found = findById(id);
        found.unpublish();
        return localBusinessRepository.save(found);
    }

    public void delete(UUID id) {
        LocalBusiness found = findById(id);

        if (rewardRepository.existsByBusinessId(id)) {
            throw new ValidationException("Cannot delete local business with linked rewards. Unpublish it instead.");
        }

        localBusinessRepository.delete(found);
    }
}
