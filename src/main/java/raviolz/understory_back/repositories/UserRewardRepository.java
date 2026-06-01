package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.UserReward;
import raviolz.understory_back.enums.UserRewardStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRewardRepository extends JpaRepository<UserReward, UUID> {

    Optional<UserReward> findByUserIdAndRewardId(UUID userId, UUID rewardId);

    boolean existsByUserIdAndRewardId(UUID userId, UUID rewardId);

    Page<UserReward> findByUserId(UUID userId, Pageable pageable);

    Page<UserReward> findByRewardId(UUID rewardId, Pageable pageable);

    Page<UserReward> findByStatus(UserRewardStatus status, Pageable pageable);

    Page<UserReward> findByUserIdAndStatus(UUID userId, UserRewardStatus status, Pageable pageable);

    boolean existsByRewardId(UUID rewardId);

    @Query("""
            SELECT ur
            FROM UserReward ur
            JOIN FETCH ur.reward r
            JOIN FETCH r.business b
            JOIN FETCH b.city
            JOIN FETCH b.businessCategory
            WHERE ur.user.id = :userId
            ORDER BY ur.unlockedAt DESC
            """)
    List<UserReward> findUnlockedLocalShopsByUserId(UUID userId);
}