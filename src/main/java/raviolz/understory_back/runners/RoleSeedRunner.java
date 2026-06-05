package raviolz.understory_back.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import raviolz.understory_back.entities.Role;
import raviolz.understory_back.repositories.RoleRepository;

@Component
@Order(1)
public class RoleSeedRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeedRunner(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("**** ROLE SEED RUNNER STARTED ****");

        roleRepository.findByCode("USER")
                .orElseGet(() -> roleRepository.save(new Role(
                        "USER",
                        "Esploratore",
                        "Standard user who explores and completes experiences"
                )));

        roleRepository.findByCode("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(
                        "ADMIN",
                        "Archivista",
                        "Administrator who manages content, bookings and submissions"
                )));

        roleRepository.findByCode("SUPER_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(
                        "SUPER_ADMIN",
                        "Custode",
                        "Full-access administrator account"
                )));

        System.out.println("**** ROLE SEED RUNNER COMPLETED ****");
    }
}