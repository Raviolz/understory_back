package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.entities.City;
import raviolz.understory_back.entities.ExperienceCategory;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.CityDTO;
import raviolz.understory_back.payloads.UpdateCityDTO;
import raviolz.understory_back.repositories.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final ImageUploadService imageUploadService;
    private final ExperienceRepository experienceRepository;
    private final PointOfInterestRepository pointOfInterestRepository;
    private final LocalBusinessRepository localBusinessRepository;
    private final RewardRepository rewardRepository;

    public CityService(CityRepository cityRepository,
                       ImageUploadService imageUploadService,
                       ExperienceRepository experienceRepository,
                       PointOfInterestRepository pointOfInterestRepository,
                       LocalBusinessRepository localBusinessRepository,
                       RewardRepository rewardRepository) {
        this.cityRepository = cityRepository;
        this.imageUploadService = imageUploadService;
        this.experienceRepository = experienceRepository;
        this.pointOfInterestRepository = pointOfInterestRepository;
        this.localBusinessRepository = localBusinessRepository;
        this.rewardRepository = rewardRepository;
    }

    public City save(CityDTO body) {
        City city = new City(
                body.name(),
                body.country(),
                body.longitude(),
                body.latitude(),
                body.description()
        );

        return cityRepository.save(city);
    }

    public Page<City> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return cityRepository.findAll(pageable);
    }

    public City findById(UUID id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City with id " + id + " not found"));
    }

    public City update(UUID id, UpdateCityDTO body) {
        City found = findById(id);

        found.setName(body.name());
        found.setCountry(body.country());
        found.setLongitude(body.longitude());
        found.setLatitude(body.latitude());
        found.setDescription(body.description());

        return cityRepository.save(found);
    }

    public Page<City> findActive(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return cityRepository.findByActiveTrue(pageable);
    }

    public City findActiveById(UUID id) {
        return cityRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("City with id " + id + " not found"));
    }

    public City updateCoverImage(UUID cityId, MultipartFile file) {
        City found = findById(cityId);

        String imageUrl = imageUploadService.uploadImage(file);

        found.setCoverImageUrl(imageUrl);

        return cityRepository.save(found);
    }

    public Optional<ExperienceCategory> findDominantCategoryByCityId(UUID cityId) {
        List<ExperienceCategory> categories = experienceRepository.findDominantCategoriesByCityId(cityId);

        if (categories.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(categories.get(0));
    }

    public City publish(UUID id) {
        City found = findById(id);
        found.publish();
        return cityRepository.save(found);
    }

    public City unpublish(UUID id) {
        City found = findById(id);
        found.unpublish();
        return cityRepository.save(found);
    }

    public void delete(UUID id) {
        City found = findById(id);

        if (pointOfInterestRepository.existsByCityId(id)) {
            throw new ValidationException("Cannot delete city with linked points of interest. Unpublish it instead.");
        }

        if (localBusinessRepository.existsByCityId(id)) {
            throw new ValidationException("Cannot delete city with linked local businesses. Unpublish it instead.");
        }

        if (rewardRepository.existsByCityId(id)) {
            throw new ValidationException("Cannot delete city with linked rewards. Unpublish it instead.");
        }

        cityRepository.delete(found);
    }

}