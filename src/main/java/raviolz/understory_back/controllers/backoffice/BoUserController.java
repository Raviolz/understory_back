package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.payloads.UserDTO;
import raviolz.understory_back.services.UserService;

@RestController
@RequestMapping("/backoffice/users")
public class BoUserController {

    private final UserService userService;

    public BoUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody @Valid UserDTO body) {
        return userService.save(body);
    }
}