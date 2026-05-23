package raviolz.understory_back.controllers.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping
    public Page<UserResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "username") String sortBy) {
        return userService.findAll(page, size, sortBy)
                .map(UserResponseDTO::fromEntity);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/{userId}/admin")
    public UserResponseDTO promoteToAdmin(@PathVariable UUID userId) {
        return UserResponseDTO.fromEntity(userService.promoteToAdmin(userId));
    }


    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PatchMapping("/{userId}/user")
    public UserResponseDTO downgradeToUser(@PathVariable UUID userId) {
        return UserResponseDTO.fromEntity(userService.downgradeToUser(userId));
    }
}

