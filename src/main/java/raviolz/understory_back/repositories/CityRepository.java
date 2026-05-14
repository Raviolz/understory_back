package raviolz.understory_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.City;

import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
}
