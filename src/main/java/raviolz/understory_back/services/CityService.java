package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.City;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.payloads.CityDTO;
import raviolz.understory_back.payloads.UpdateCityDTO;
import raviolz.understory_back.repositories.CityRepository;

import java.util.UUID;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City save(CityDTO body) {
        City city = new City(
                body.name(),
                body.country(),
                body.longitude(),
                body.latitude(),
                body.description(),
                body.coverImageUrl()
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

}