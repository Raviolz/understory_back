package raviolz.understory_back.controllers.backoffice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.Reward;
import raviolz.understory_back.enums.RewardType;
import raviolz.understory_back.payloads.RewardDTO;
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
    public Page<Reward> findAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findAll(page, size, sortBy);
    }

    @GetMapping("/{rewardId}")
    public Reward findById(@PathVariable UUID rewardId) {
        return rewardService.findById(rewardId);
    }

    @GetMapping("/business/{businessId}")
    public Page<Reward> findByBusiness(@PathVariable UUID businessId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findByBusiness(businessId, page, size, sortBy);
    }

    @GetMapping("/city/{cityId}")
    public Page<Reward> findByCity(@PathVariable UUID cityId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findByCity(cityId, page, size, sortBy);
    }

    @GetMapping("/type/{rewardType}")
    public Page<Reward> findByRewardType(@PathVariable RewardType rewardType,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "title") String sortBy) {
        return rewardService.findByRewardType(rewardType, page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reward save(@RequestBody @Valid RewardDTO body) {
        return rewardService.save(body);
    }

    @PutMapping("/{rewardId}")
    public Reward update(@PathVariable UUID rewardId,
                         @RequestBody @Valid RewardDTO body) {
        return rewardService.update(rewardId, body);
    }

    @PatchMapping("/{rewardId}/publish")
    public Reward publish(@PathVariable UUID rewardId) {
        return rewardService.publish(rewardId);
    }

    @PatchMapping("/{rewardId}/unpublish")
    public Reward unpublish(@PathVariable UUID rewardId) {
        return rewardService.unpublish(rewardId);
    }
}