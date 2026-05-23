package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.enums.RewardType;
import raviolz.understory_back.payloads.RewardDTO;
import raviolz.understory_back.payloads.responses.BoRewardResponseDTO;
import raviolz.understory_back.services.RewardService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/rewards")
public class BoRewardController {

    private final RewardService rewardService;

    public BoRewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public Page<BoRewardResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findAll(page, size, sortBy)
                .map(BoRewardResponseDTO::fromEntity);
    }

    @GetMapping("/{rewardId}")
    public BoRewardResponseDTO findById(@PathVariable UUID rewardId) {
        return BoRewardResponseDTO.fromEntity(
                rewardService.findById(rewardId)
        );
    }

    @GetMapping("/business/{businessId}")
    public Page<BoRewardResponseDTO> findByBusiness(@PathVariable UUID businessId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findByBusiness(businessId, page, size, sortBy)
                .map(BoRewardResponseDTO::fromEntity);
    }

    @GetMapping("/city/{cityId}")
    public Page<BoRewardResponseDTO> findByCity(@PathVariable UUID cityId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findByCity(cityId, page, size, sortBy)
                .map(BoRewardResponseDTO::fromEntity);
    }

    @GetMapping("/type/{rewardType}")
    public Page<BoRewardResponseDTO> findByRewardType(@PathVariable RewardType rewardType,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findByRewardType(rewardType, page, size, sortBy)
                .map(BoRewardResponseDTO::fromEntity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoRewardResponseDTO save(@RequestBody @Valid RewardDTO body) {
        return BoRewardResponseDTO.fromEntity(
                rewardService.save(body)
        );
    }

    @PutMapping("/{rewardId}")
    public BoRewardResponseDTO update(@PathVariable UUID rewardId,
                                      @RequestBody @Valid RewardDTO body) {
        return BoRewardResponseDTO.fromEntity(
                rewardService.update(rewardId, body)
        );
    }

    @PatchMapping("/{rewardId}/publish")
    public BoRewardResponseDTO publish(@PathVariable UUID rewardId) {
        return BoRewardResponseDTO.fromEntity(
                rewardService.publish(rewardId)
        );
    }

    @PatchMapping("/{rewardId}/unpublish")
    public BoRewardResponseDTO unpublish(@PathVariable UUID rewardId) {
        return BoRewardResponseDTO.fromEntity(
                rewardService.unpublish(rewardId)
        );
    }
}