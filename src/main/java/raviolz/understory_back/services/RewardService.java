package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.City;
import raviolz.understory_back.entities.LocalBusiness;
import raviolz.understory_back.entities.Reward;
import raviolz.understory_back.enums.RewardType;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.RewardDTO;
import raviolz.understory_back.repositories.RewardRepository;

import java.util.UUID;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;
    private final LocalBusinessService localBusinessService;
    private final CityService cityService;

    public RewardService(
            RewardRepository rewardRepository,
            LocalBusinessService localBusinessService,
            CityService cityService
    ) {
        this.rewardRepository = rewardRepository;
        this.localBusinessService = localBusinessService;
        this.cityService = cityService;
    }

    public Reward save(RewardDTO body) {
        LocalBusiness business = localBusinessService.findById(body.businessId());
        City city = cityService.findById(body.cityId());

        if (!business.getCity().getId().equals(city.getId())) {
            throw new ValidationException("Business does not belong to selected city");
        }

        Reward reward = new Reward(
                business,
                city,
                body.title(),
                body.description(),
                body.discountCode(),
                body.rewardType(),
                body.validFrom(),
                body.validUntil()
        );

        return rewardRepository.save(reward);
    }

    public Page<Reward> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rewardRepository.findAll(pageable);
    }

    public Page<Reward> findActive(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rewardRepository.findByActiveTrue(pageable);
    }

    public Reward findById(UUID id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reward with id " + id + " not found"));
    }

    public Page<Reward> findByBusiness(UUID businessId, int page, int size, String sortBy) {
        localBusinessService.findById(businessId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rewardRepository.findByBusinessId(businessId, pageable);
    }

    public Page<Reward> findByCity(UUID cityId, int page, int size, String sortBy) {
        cityService.findById(cityId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rewardRepository.findByCityId(cityId, pageable);
    }

    public Page<Reward> findByRewardType(RewardType rewardType, int page, int size, String sortBy) {
        if (rewardType == null) {
            throw new ValidationException("Reward type is required");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return rewardRepository.findByRewardType(rewardType, pageable);
    }

    public Reward update(UUID id, RewardDTO body) {
        Reward found = findById(id);

        LocalBusiness business = localBusinessService.findById(body.businessId());
        City city = cityService.findById(body.cityId());

        if (!business.getCity().getId().equals(city.getId())) {
            throw new ValidationException("Business does not belong to selected city");
        }

        found.setBusiness(business);
        found.setCity(city);
        found.setTitle(body.title());
        found.setDescription(body.description());
        found.setDiscountCode(body.discountCode());
        found.setRewardType(body.rewardType());
        found.setValidityPeriod(body.validFrom(), body.validUntil());

        return rewardRepository.save(found);
    }

    public Reward publish(UUID id) {
        Reward found = findById(id);
        found.publish();
        return rewardRepository.save(found);
    }

    public Reward unpublish(UUID id) {
        Reward found = findById(id);
        found.unpublish();
        return rewardRepository.save(found);
    }
}