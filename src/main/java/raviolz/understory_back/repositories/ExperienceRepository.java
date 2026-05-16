package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.Experience;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, UUID> {

    Page<Experience> findByActiveTrue(Pageable pageable);

    Optional<Experience> findByIdAndActiveTrue(UUID id);

    Page<Experience> findByPointOfInterestIdAndActiveTrue(UUID pointOfInterestId, Pageable pageable);
}
