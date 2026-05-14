package raviolz.understory_back.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Experience;
import raviolz.understory_back.entities.UploadGame;
import raviolz.understory_back.enums.GameType;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.UpdateUploadGameDTO;
import raviolz.understory_back.payloads.UploadGameDTO;
import raviolz.understory_back.repositories.UploadGameRepository;

import java.util.UUID;

@Service
public class UploadGameService {

    private final UploadGameRepository uploadGameRepository;
    private final ExperienceService experienceService;

    public UploadGameService(UploadGameRepository uploadGameRepository, ExperienceService experienceService) {
        this.uploadGameRepository = uploadGameRepository;
        this.experienceService = experienceService;
    }

    public UploadGame save(UploadGameDTO body) {
        Experience experience = experienceService.findById(body.experienceId());

        if (experience.getGameType() != GameType.IMAGE_UPLOAD) {
            throw new ValidationException("Experience " + body.experienceId() + " is not an upload experience");
        }

        if (uploadGameRepository.existsByExperienceId(body.experienceId())) {
            throw new ValidationException("Upload game already exists for experience " + body.experienceId());
        }

        UploadGame uploadGame = new UploadGame(
                experience,
                body.promptText(),
                body.validationHint(),
                body.targetDescription(),
                body.referenceImageUrl()
        );

        return uploadGameRepository.save(uploadGame);
    }

    public Page<UploadGame> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return uploadGameRepository.findAll(pageable);
    }

    public UploadGame findById(UUID id) {
        return uploadGameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Upload game with id " + id + " not found"));
    }

    public UploadGame findByExperienceId(UUID experienceId) {
        return uploadGameRepository.findByExperienceId(experienceId)
                .orElseThrow(() -> new NotFoundException("Upload game for experience " + experienceId + " not found"));
    }

    public UploadGame update(UUID id, UpdateUploadGameDTO body) {
        UploadGame found = findById(id);

        found.setPromptText(body.promptText());
        found.setValidationHint(body.validationHint());
        found.setTargetDescription(body.targetDescription());
        found.setReferenceImageUrl(body.referenceImageUrl());

        return uploadGameRepository.save(found);
    }
}