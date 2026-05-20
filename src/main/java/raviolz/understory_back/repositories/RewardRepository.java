package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.Reward;
import raviolz.understory_back.enums.RewardType;

import java.util.List;
import java.util.UUID;

@Repository
public interface RewardRepository extends JpaRepository<Reward, UUID> {

    Page<Reward> findByBusinessId(UUID businessId, Pageable pageable);

    Page<Reward> findByExperienceId(UUID experienceId, Pageable pageable);

    Page<Reward> findByRewardType(RewardType rewardType, Pageable pageable);

    Page<Reward> findByActiveTrue(Pageable pageable);

    List<Reward> findByCityIdAndActiveTrue(UUID cityId);
}