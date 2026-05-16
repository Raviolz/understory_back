package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.PointOfInterest;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PointOfInterestRepository extends JpaRepository<PointOfInterest, UUID> {

    Page<PointOfInterest> findByActiveTrue(Pageable pageable);

    Optional<PointOfInterest> findByIdAndActiveTrue(UUID id);

    Page<PointOfInterest> findByCityIdAndActiveTrue(UUID cityId, Pageable pageable);
}