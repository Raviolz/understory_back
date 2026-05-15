package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.UserUploadSubmission;
import raviolz.understory_back.enums.UploadSubmissionStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserUploadSubmissionRepository extends JpaRepository<UserUploadSubmission, UUID> {

    Optional<UserUploadSubmission> findByUserIdAndExperienceId(UUID userId, UUID experienceId);

    Page<UserUploadSubmission> findByUserId(UUID userId, Pageable pageable);

    Page<UserUploadSubmission> findByExperienceId(UUID experienceId, Pageable pageable);

    Page<UserUploadSubmission> findByStatus(UploadSubmissionStatus status, Pageable pageable);

}