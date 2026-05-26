package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.UserExperienceProgress;
import raviolz.understory_back.enums.ProgressStatus;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserExperienceProgressRepository extends JpaRepository<UserExperienceProgress, UUID> {

    Optional<UserExperienceProgress> findByUserIdAndExperienceId(UUID userId, UUID experienceId);

    Page<UserExperienceProgress> findByUserId(UUID userId, Pageable pageable);

    Page<UserExperienceProgress> findByUserIdAndStatus(UUID userId, ProgressStatus status, Pageable pageable);

    long countByUserIdAndStatusAndExperiencePointOfInterestCityId(UUID userId, ProgressStatus status, UUID cityId);
}