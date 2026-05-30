package raviolz.understory_back.controllers.me;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.payloads.responses.CityKnowledgeResponseDTO;
import raviolz.understory_back.services.UserExperienceProgressService;

import java.util.List;

@RestController
@RequestMapping("/me")
public class MeCityKnowledgeController {

    private final UserExperienceProgressService userExperienceProgressService;

    public MeCityKnowledgeController(UserExperienceProgressService userExperienceProgressService) {
        this.userExperienceProgressService = userExperienceProgressService;
    }

    @GetMapping("/city-knowledge")
    public List<CityKnowledgeResponseDTO> getMyCityKnowledge(@AuthenticationPrincipal User currentUser) {
        return userExperienceProgressService.getCityKnowledgeForUser(currentUser.getId());
    }
}