package raviolz.understory_back.controllers.me;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.payloads.responses.UserAtlasEntryResponseDTO;
import raviolz.understory_back.services.UserExperienceProgressService;

@RestController
@RequestMapping("/me/atlas")
public class MeAtlasController {

    private final UserExperienceProgressService userExperienceProgressService;

    public MeAtlasController(UserExperienceProgressService userExperienceProgressService) {
        this.userExperienceProgressService = userExperienceProgressService;
    }

    @GetMapping
    public Page<UserAtlasEntryResponseDTO> getMyAtlas(@AuthenticationPrincipal User currentUser,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "completedAt") String sortBy) {
        return userExperienceProgressService.findAtlasByUser(
                currentUser.getId(),
                page,
                size,
                sortBy
        );
    }
}