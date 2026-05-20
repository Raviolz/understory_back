package raviolz.understory_back.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raviolz.understory_back.entities.Role;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.repositories.UserRepository;
import raviolz.understory_back.services.RoleService;

@Component
@Order(2)
public class SuperAdminSeedRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder bcrypt;

    public SuperAdminSeedRunner(UserRepository userRepository,
                                RoleService roleService,
                                PasswordEncoder bcrypt) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bcrypt = bcrypt;
    }

    @Override
    public void run(String... args) {
        System.out.println("**** SUPER ADMIN SEED RUNNER STARTED ****");

        if (!userRepository.existsByEmail("superadmin@understory.local")) {
            Role superAdminRole = roleService.findByCode("SUPER_ADMIN");

            User superAdmin = new User(
                    "superadmin",
                    "Super",
                    "Admin",
                    "superadmin@understory.com",
                    bcrypt.encode("superadmin123"),
                    superAdminRole
            );

            userRepository.save(superAdmin);
        }

        System.out.println("**** SUPER ADMIN SEED RUNNER COMPLETED ****");
    }
}