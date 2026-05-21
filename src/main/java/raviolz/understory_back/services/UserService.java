package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.entities.Role;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.exceptions.InternalServerException;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.UpdateUserProfileDTO;
import raviolz.understory_back.payloads.UserDTO;
import raviolz.understory_back.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder bcrypt;
    private final ImageUploadService imageUploadService;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder bcrypt, ImageUploadService imageUploadService, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bcrypt = bcrypt;
        this.imageUploadService = imageUploadService;
        this.emailService = emailService;

    }

    public User save(UserDTO body) {
        if (userRepository.existsByEmail(body.email().trim().toLowerCase())) {
            throw new ValidationException("Email " + body.email() + " already in use");
        }

        if (userRepository.existsByUsername(body.username().trim())) {
            throw new ValidationException("Username " + body.username() + " already in use");
        }

        Role userRole = roleService.findByCode("USER");

        User user = new User(
                body.username(),
                body.name(),
                body.surname(),
                body.email(),
                bcrypt.encode(body.password()),
                userRole
        );

        User savedUser = userRepository.save(user);
        try {
            emailService.sendRegistrationEmail(savedUser);
        } catch (InternalServerException ex) {
            System.out.println("Email di registrazione non inviata: " + ex.getMessage());
        }

        return savedUser;

    }

    public Page<User> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    public User updateProfile(UUID id, UpdateUserProfileDTO body) {
        User found = findById(id);

        String normalizedUsername = body.username().trim();

        if (!found.getUsername().equals(normalizedUsername) &&
                userRepository.existsByUsername(normalizedUsername)) {
            throw new ValidationException("Username " + body.username() + " already in use");
        }

        found.setUsername(body.username());
        found.setName(body.name());
        found.setSurname(body.surname());

        return userRepository.save(found);
    }


    public User updateAvatar(UUID userId, MultipartFile file) {
        User found = findById(userId);

        String avatarUrl = imageUploadService.uploadImage(file);

        found.setAvatarUrl(avatarUrl);

        return userRepository.save(found);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
    }

    public User promoteToAdmin(UUID userId) {
        User found = findById(userId);

        Role adminRole = roleService.findByCode("ADMIN");

        found.setRole(adminRole);

        return userRepository.save(found);
    }

    public User downgradeToUser(UUID userId) {
        User found = findById(userId);

        Role userRole = roleService.findByCode("USER");

        found.setRole(userRole);

        return userRepository.save(found);
    }
}