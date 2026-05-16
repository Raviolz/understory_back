package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.City;
import raviolz.understory_back.entities.PointOfInterest;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.payloads.PointOfInterestDTO;
import raviolz.understory_back.payloads.UpdatePointOfInterestDTO;
import raviolz.understory_back.repositories.PointOfInterestRepository;

import java.util.UUID;

@Service
public class PointOfInterestService {

    private final PointOfInterestRepository pointOfInterestRepository;
    private final CityService cityService;

    public PointOfInterestService(PointOfInterestRepository pointOfInterestRepository, CityService cityService) {
        this.pointOfInterestRepository = pointOfInterestRepository;
        this.cityService = cityService;
    }

    public PointOfInterest save(PointOfInterestDTO body) {
        City city = cityService.findById(body.cityId());

        PointOfInterest poi = new PointOfInterest(
                city,
                body.name(),
                body.shortDescription(),
                body.imageUrl(),
                body.longitude(),
                body.latitude()
        );

        return pointOfInterestRepository.save(poi);
    }

    public Page<PointOfInterest> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return pointOfInterestRepository.findAll(pageable);
    }

    public PointOfInterest findById(UUID id) {
        return pointOfInterestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Point of interest with id " + id + " not found"));
    }

    public PointOfInterest findActiveById(UUID id) {
        return pointOfInterestRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Active point of interest with id " + id + " not found"));
    }

    public PointOfInterest update(UUID id, UpdatePointOfInterestDTO body) {
        PointOfInterest found = findById(id);
        City city = cityService.findById(body.cityId());

        found.setCity(city);
        found.setName(body.name());
        found.setShortDescription(body.shortDescription());
        found.setLongitude(body.longitude());
        found.setLatitude(body.latitude());

        return pointOfInterestRepository.save(found);
    }

    public Page<PointOfInterest> findActiveByCity(UUID cityId, int page, int size, String sortBy) {
        cityService.findById(cityId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return pointOfInterestRepository.findByCityIdAndActiveTrue(cityId, pageable);
    }

    public PointOfInterest publish(UUID id) {
        PointOfInterest found = findById(id);
        found.publish();
        return pointOfInterestRepository.save(found);
    }

    public PointOfInterest unpublish(UUID id) {
        PointOfInterest found = findById(id);
        found.unpublish();
        return pointOfInterestRepository.save(found);
    }
}