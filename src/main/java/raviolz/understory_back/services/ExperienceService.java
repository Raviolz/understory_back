package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.ExperienceCategory;
import raviolz.understory_back.entities.PointOfInterest;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.payloads.ExperienceDTO;
import raviolz.understory_back.payloads.UpdateExperienceDTO;
import raviolz.understory_back.repositories.ExperienceRepository;

import java.util.UUID;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final PointOfInterestService pointOfInterestService;
    private final ExperienceCategoryService experienceCategoryService;

    public ExperienceService(ExperienceRepository experienceRepository, PointOfInterestService pointOfInterestService, ExperienceCategoryService experienceCategoryService
    ) {
        this.experienceRepository = experienceRepository;
        this.pointOfInterestService = pointOfInterestService;
        this.experienceCategoryService = experienceCategoryService;
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
                body.revealTitle(),
                body.revealImageUrl(),
                body.revealText(),
                body.journalText(),
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
        found.setRevealTitle(body.revealTitle());
        found.setRevealText(body.revealText());
        found.setJournalText(body.journalText());
        found.setXpReward(body.xpReward());
        found.setDifficulty(body.difficulty());

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

}
