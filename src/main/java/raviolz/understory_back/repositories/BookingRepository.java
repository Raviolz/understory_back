package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.Booking;
import raviolz.understory_back.enums.BookingStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Page<Booking> findByUserRewardUserId(UUID userId, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    Page<Booking> findByUserRewardRewardBusinessId(UUID businessId, Pageable pageable);

    Optional<Booking> findByUserRewardId(UUID userRewardId);

    boolean existsByUserRewardId(UUID userRewardId);
}