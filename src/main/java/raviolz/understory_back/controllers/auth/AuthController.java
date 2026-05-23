package raviolz.understory_back.controllers.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.payloads.LoginDTO;
import raviolz.understory_back.payloads.UserDTO;
import raviolz.understory_back.payloads.responses.LoginResponseDTO;
import raviolz.understory_back.payloads.responses.UserResponseDTO;
import raviolz.understory_back.services.AuthService;
import raviolz.understory_back.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid UserDTO body) {
        return UserResponseDTO.fromEntity(userService.save(body));
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO body) {
        String token = authService.checkCredentialAndGenerateToken(body);
        return new LoginResponseDTO(token);
    }
}