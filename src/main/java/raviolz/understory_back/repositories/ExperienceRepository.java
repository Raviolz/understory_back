package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.ExperienceCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, UUID> {

    Page<Experience> findByActiveTrue(Pageable pageable);

    Optional<Experience> findByIdAndActiveTrue(UUID id);

    Page<Experience> findByPointOfInterestIdAndActiveTrue(UUID pointOfInterestId, Pageable pageable);

    @Query("""
            SELECT e.experienceCategory
            FROM Experience e
            WHERE e.active = true
            AND e.pointOfInterest.active = true
            AND e.pointOfInterest.city.id = :cityId
            GROUP BY e.experienceCategory
            ORDER BY COUNT(e) DESC
            """)
    List<ExperienceCategory> findDominantCategoriesByCityId(@Param("cityId") UUID cityId);

    Optional<Experience> findFirstByPointOfInterestIdAndActiveTrueOrderByTitleAsc(UUID pointOfInterestId);

}
