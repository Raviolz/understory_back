package raviolz.understory_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raviolz.understory_back.entities.BusinessCategory;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory, UUID> {

    Optional<BusinessCategory> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByLabel(String label);
}