package raviolz.understory_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.UploadGame;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UploadGameRepository extends JpaRepository<UploadGame, UUID> {

    Optional<UploadGame> findByExperienceId(UUID experienceId);

    boolean existsByExperienceId(UUID experienceId);
}
