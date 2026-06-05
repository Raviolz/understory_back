package raviolz.understory_back.runners;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import raviolz.understory_back.entities.Role;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.repositories.UserRepository;
import raviolz.understory_back.services.RoleService;

@Component
@Order(2)
public class AdminSeedRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder bcrypt;

    private final String superAdminEmail;
    private final String superAdminPassword;

    private final String adminOneEmail;
    private final String adminOnePassword;

    private final String adminTwoEmail;
    private final String adminTwoPassword;

    private final String adminThreeEmail;
    private final String adminThreePassword;

    public AdminSeedRunner(UserRepository userRepository,
                           RoleService roleService,
                           PasswordEncoder bcrypt,
                           @Value("${seed.superadmin.email}") String superAdminEmail,
                           @Value("${seed.superadmin.password}") String superAdminPassword,
                           @Value("${seed.admin.one.email}") String adminOneEmail,
                           @Value("${seed.admin.one.password}") String adminOnePassword,
                           @Value("${seed.admin.two.email}") String adminTwoEmail,
                           @Value("${seed.admin.two.password}") String adminTwoPassword,
                           @Value("${seed.admin.three.email}") String adminThreeEmail,
                           @Value("${seed.admin.three.password}") String adminThreePassword) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bcrypt = bcrypt;
        this.superAdminEmail = superAdminEmail;
        this.superAdminPassword = superAdminPassword;
        this.adminOneEmail = adminOneEmail;
        this.adminOnePassword = adminOnePassword;
        this.adminTwoEmail = adminTwoEmail;
        this.adminTwoPassword = adminTwoPassword;
        this.adminThreeEmail = adminThreeEmail;
        this.adminThreePassword = adminThreePassword;
    }

    @Override
    public void run(String... args) {
        System.out.println("**** ADMIN SEED RUNNER STARTED ****");

        Role superAdminRole = roleService.findByCode("SUPER_ADMIN");
        Role adminRole = roleService.findByCode("ADMIN");

        createUserIfMissing(
                "superadmin",
                "Super",
                "Admin",
                superAdminEmail,
                superAdminPassword,
                superAdminRole
        );

        createUserIfMissing(
                "admin1",
                "Admin",
                "One",
                adminOneEmail,
                adminOnePassword,
                adminRole
        );

        createUserIfMissing(
                "admin2",
                "Admin",
                "Two",
                adminTwoEmail,
                adminTwoPassword,
                adminRole
        );

        createUserIfMissing(
                "admin3",
                "Admin",
                "Three",
                adminThreeEmail,
                adminThreePassword,
                adminRole
        );

        System.out.println("**** ADMIN SEED RUNNER COMPLETED ****");
    }

    private void createUserIfMissing(String username,
                                     String name,
                                     String surname,
                                     String email,
                                     String password,
                                     Role role) {
        validateSeedCredentials(email, password);

        String normalizedEmail = email.trim().toLowerCase();
        String normalizedUsername = username.trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            return;
        }

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new ValidationException("Seed username " + normalizedUsername + " already exists with another email");
        }

        User user = new User(
                normalizedUsername,
                name,
                surname,
                normalizedEmail,
                bcrypt.encode(password),
                role
        );

        userRepository.save(user);
    }

    private void validateSeedCredentials(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Seed email is required");
        }

        if (password == null || password.isBlank()) {
            throw new ValidationException("Seed password is required");
        }

        if (password.length() < 8) {
            throw new ValidationException("Seed password must contain at least 8 characters");
        }
    }
}