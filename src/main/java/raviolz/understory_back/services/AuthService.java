package raviolz.understory_back.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.UnauthorizedException;
import raviolz.understory_back.payloads.LoginDTO;
import raviolz.understory_back.security.TokenTools;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UserService userService, TokenTools tokenTools, PasswordEncoder bcrypt) {
        this.userService = userService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialAndGenerateToken(LoginDTO body) {
        try {
            User found = userService.findByEmail(body.email());

            if (bcrypt.matches(body.password(), found.getPassword())) {
                return tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Credenziali errate");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}