package raviolz.understory_back.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.entities.UserExperienceProgress;
import raviolz.understory_back.enums.ProgressStatus;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.UserExperienceProgressDTO;
import raviolz.understory_back.payloads.UserNoteDTO;
import raviolz.understory_back.repositories.UserExperienceProgressRepository;
import raviolz.understory_back.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserExperienceProgressService {

    private final UserExperienceProgressRepository userExperienceProgressRepository;
    private final UserService userService;
    private final ExperienceService experienceService;
    private final UserRepository userRepository;

    public UserExperienceProgressService(UserExperienceProgressRepository userExperienceProgressRepository, UserService userService, ExperienceService experienceService, UserRepository userRepository

    ) {
        this.userExperienceProgressRepository = userExperienceProgressRepository;
        this.userService = userService;
        this.experienceService = experienceService;
        this.userRepository = userRepository;
    }

    public UserExperienceProgress startOrGet(UserExperienceProgressDTO body) {
        User user = userService.findById(body.userId());
        Experience experience = experienceService.findById(body.experienceId());

        return userExperienceProgressRepository
                .findByUserIdAndExperienceId(body.userId(), body.experienceId())
                .orElseGet(() -> userExperienceProgressRepository.save(
                        new UserExperienceProgress(user, experience)
                ));
    }

    public Page<UserExperienceProgress> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userExperienceProgressRepository.findAll(pageable);
    }

    public UserExperienceProgress findById(UUID id) {
        return userExperienceProgressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User experience progress with id " + id + " not found"));
    }

    public UserExperienceProgress findByUserAndExperience(UUID userId, UUID experienceId) {
        return userExperienceProgressRepository.findByUserIdAndExperienceId(userId, experienceId)
                .orElseThrow(() -> new NotFoundException(
                        "Progress for user " + userId + " and experience " + experienceId + " not found"
                ));
    }

    public Page<UserExperienceProgress> findByUser(UUID userId, int page, int size, String sortBy) {
        userService.findById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userExperienceProgressRepository.findByUserId(userId, pageable);
    }

    public Page<UserExperienceProgress> findCompletedByUser(UUID userId, int page, int size, String sortBy) {
        userService.findById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userExperienceProgressRepository.findByUserIdAndStatus(userId, ProgressStatus.COMPLETED, pageable);
    }

    public UserExperienceProgress updateUserNote(UUID progressId, UserNoteDTO body) {
        UserExperienceProgress found = findById(progressId);

        found.updateUserNote(body.userNote());

        return userExperienceProgressRepository.save(found);
    }


    // Mi aiuta ad avere la certezza che l operazione non si concluda a meta' o bug strani --> Considero come unica operazione o tutto ok o fallisce
    // Tutto quello che succede in questo metodo deve riuscire insieme. Se qualcosa fallisce, torna tutto com'era prima.
    @Transactional

    // Ritorna gli XP guadagnati in questa chiamata. Se la experience era già completata e premiata, ritorna 0. Mi evita un passaggio in piu' per recuperare gli xp
    // se avessi tornato solo un boolean, cosi' e' implicito --> 0 false, > 0 --> true
    public int completeAndAwardXp(UUID userId, UUID experienceId) {
        User user = userService.findById(userId);
        Experience experience = experienceService.findById(experienceId);
//        Cerca il progress di questo user per questa experience. Se esiste, usalo. Se non esiste, crealo e salvalo.
//        In mano avro' sicuramente una UserExperienceProgress entity : progress
        UserExperienceProgress progress = userExperienceProgressRepository
                .findByUserIdAndExperienceId(userId, experienceId)
                .orElseGet(() -> userExperienceProgressRepository.save(
                        new UserExperienceProgress(user, experience)
                ));

        progress.complete();

        if (progress.isXpAwarded()) {
            userExperienceProgressRepository.save(progress);
            return 0;
        }

        user.addXp(experience.getXpReward());
        progress.markXpAwarded();

        userRepository.save(user);
        // Avendo cambiato gli XP devo salvare lo user modificato
        userExperienceProgressRepository.save(progress);

        return experience.getXpReward();
    }


    public UserExperienceProgress updateUserNoteForUser(UUID userId, UUID progressId, UserNoteDTO body) {
        UserExperienceProgress found = findById(progressId);

        if (!found.getUser().getId().equals(userId)) {
            throw new ValidationException("You cannot update another user's progress note");
        }

        found.updateUserNote(body.userNote());

        return userExperienceProgressRepository.save(found);
    }

    public long countCompletedByUserAndCity(UUID userId, UUID cityId) {
        return userExperienceProgressRepository.countByUserIdAndStatusAndExperiencePointOfInterestCityId(
                userId,
                ProgressStatus.COMPLETED,
                cityId
        );
    }
}
