package raviolz.understory_back.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.LocalBusiness;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocalBusinessRepository extends JpaRepository<LocalBusiness, UUID> {

    Optional<LocalBusiness> findByNameAndAddressAndCityId(String name, String address, UUID cityId);

    boolean existsByNameAndAddressAndCityId(String name, String address, UUID cityId);

    Page<LocalBusiness> findByCityId(UUID cityId, Pageable pageable);

    Page<LocalBusiness> findByBusinessCategoryId(UUID businessCategoryId, Pageable pageable);

    Page<LocalBusiness> findByActiveTrue(Pageable pageable);

    boolean existsByCityId(UUID cityId);

    boolean existsByBusinessCategoryId(UUID businessCategoryId);

    Optional<LocalBusiness> findByIdAndActiveTrue(UUID id);

    Page<LocalBusiness> findByCityIdAndActiveTrue(UUID cityId, Pageable pageable);

    Page<LocalBusiness> findByBusinessCategoryIdAndActiveTrue(UUID businessCategoryId, Pageable pageable);
}
