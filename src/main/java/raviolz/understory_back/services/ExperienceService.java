package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.ExperienceCategory;
import raviolz.understory_back.entities.PointOfInterest;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.ExperienceDTO;
import raviolz.understory_back.payloads.UpdateExperienceDTO;
import raviolz.understory_back.repositories.*;

import java.util.UUID;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final PointOfInterestService pointOfInterestService;
    private final ExperienceCategoryService experienceCategoryService;
    private final ImageUploadService imageUploadService;
    private final UserExperienceProgressRepository userExperienceProgressRepository;
    private final UserUploadSubmissionRepository userUploadSubmissionRepository;
    private final QuizGameRepository quizGameRepository;
    private final UploadGameRepository uploadGameRepository;

    public ExperienceService(ExperienceRepository experienceRepository,
                             PointOfInterestService pointOfInterestService,
                             ExperienceCategoryService experienceCategoryService,
                             ImageUploadService imageUploadService,
                             UserExperienceProgressRepository userExperienceProgressRepository,
                             UserUploadSubmissionRepository userUploadSubmissionRepository,
                             QuizGameRepository quizGameRepository,
                             UploadGameRepository uploadGameRepository
    ) {
        this.experienceRepository = experienceRepository;
        this.pointOfInterestService = pointOfInterestService;
        this.experienceCategoryService = experienceCategoryService;
        this.imageUploadService = imageUploadService;
        this.userExperienceProgressRepository = userExperienceProgressRepository;
        this.userUploadSubmissionRepository = userUploadSubmissionRepository;
        this.quizGameRepository = quizGameRepository;
        this.uploadGameRepository = uploadGameRepository;
    }

    public Experience save(ExperienceDTO body) {
        PointOfInterest pointOfInterest = pointOfInterestService.findById(body.pointOfInterestId());

        ExperienceCategory experienceCategory = experienceCategoryService.findById(body.experienceCategoryId());

        Experience experience = new Experience(
                pointOfInterest,
                body.title(),
                body.gameType(),
                body.hookText(),
                body.introText(),
                body.contextText(),
                body.leadInText(),
                body.revealTitle(),
                body.revealText(),
                body.journalText(),
                body.atlasText(),
                body.xpReward(),
                body.difficulty(),
                experienceCategory
        );

        return experienceRepository.save(experience);
    }

    public Page<Experience> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return experienceRepository.findAll(pageable);
    }

    public Experience findById(UUID id) {
        return experienceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Experience with id " + id + " not found"));
    }

    public Experience update(UUID id, UpdateExperienceDTO body) {
        Experience found = findById(id);

        PointOfInterest pointOfInterest = pointOfInterestService.findById(body.pointOfInterestId());

        ExperienceCategory experienceCategory = experienceCategoryService.findById(body.experienceCategoryId());

        found.setPointOfInterest(pointOfInterest);
        found.setExperienceCategory(experienceCategory);
        found.setTitle(body.title());
        found.setGameType(body.gameType());
        found.setHookText(body.hookText());
        found.setIntroText(body.introText());
        found.setContextText(body.contextText());
        found.setLeadInText(body.leadInText());
        found.setRevealTitle(body.revealTitle());
        found.setRevealText(body.revealText());
        found.setJournalText(body.journalText());
        found.setAtlasText(body.atlasText());
        found.setXpReward(body.xpReward());
        found.setDifficulty(body.difficulty());

        return experienceRepository.save(found);
    }

    public Experience updateRevealImage(UUID experienceId, MultipartFile file) {
        Experience found = findById(experienceId);

        String imageUrl = imageUploadService.uploadImage(file);

        found.setRevealImageUrl(imageUrl);

        return experienceRepository.save(found);
    }

    public Page<Experience> findActiveByPointOfInterest(UUID pointOfInterestId, int page, int size, String sortBy) {
        pointOfInterestService.findById(pointOfInterestId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return experienceRepository.findByPointOfInterestIdAndActiveTrue(pointOfInterestId, pageable);
    }


    public Experience findActiveById(UUID id) {
        return experienceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Experience with id " + id + " not found"));
    }

    public Experience publish(UUID id) {
        Experience found = findById(id);
        found.publish();
        return experienceRepository.save(found);
    }

    public Experience unpublish(UUID id) {
        Experience found = findById(id);
        found.unpublish();
        return experienceRepository.save(found);
    }


    public void delete(UUID id) {
        Experience found = findById(id);

        if (userExperienceProgressRepository.existsByExperienceId(id)) {
            throw new ValidationException("Cannot delete experience with user progress. Unpublish it instead.");
        }

        if (userUploadSubmissionRepository.existsByExperienceId(id)) {
            throw new ValidationException("Cannot delete experience with user upload submissions. Unpublish it instead.");
        }

        if (quizGameRepository.existsByExperienceId(id)) {
            throw new ValidationException("Cannot delete experience with linked quiz game. Delete the quiz game first or unpublish the experience.");
        }

        if (uploadGameRepository.existsByExperienceId(id)) {
            throw new ValidationException("Cannot delete experience with linked upload game. Delete the upload game first or unpublish the experience.");
        }

        experienceRepository.delete(found);
    }
}
