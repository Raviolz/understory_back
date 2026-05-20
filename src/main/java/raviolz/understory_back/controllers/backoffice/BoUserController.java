package raviolz.understory_back.controllers.backoffice;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raviolz.understory_back.payloads.responses.UserResponseDTO;
import raviolz.understory_back.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/users")
public class BoUserController {

    private final UserService userService;

    public BoUserController(UserService userService) {
        this.userService = userService;
    }


    // VECCHIO PER TEST PRE AUTH
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public User save(@RequestBody @Valid UserDTO body) {
//        return userService.save(body);
//    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/{userId}/admin")
    public UserResponseDTO promoteToAdmin(@PathVariable UUID userId) {
        return UserResponseDTO.fromEntity(userService.promoteToAdmin(userId));
    }


    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping
    UserResponseDTO downgradeToUser(@PathVariable UUID userId) {
        return UserResponseDTO.fromEntity(userService.downgradeToUser(userId));
    }
}

