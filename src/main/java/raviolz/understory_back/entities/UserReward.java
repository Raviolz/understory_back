package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.enums.UserRewardStatus;
import raviolz.understory_back.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "user_rewards",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "reward_id"})
        }
)
public class UserReward {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;

    @Column(name = "unlocked_at", nullable = false)
    private LocalDateTime unlockedAt;

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UserRewardStatus status;

    public UserReward(User user, Reward reward) {
        setUser(user);
        setReward(reward);
        this.unlockedAt = LocalDateTime.now();
        this.redeemedAt = null;
        this.status = UserRewardStatus.UNLOCKED;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new ValidationException("User is required");
        }

        this.user = user;
    }

    public void setReward(Reward reward) {
        if (reward == null) {
            throw new ValidationException("Reward is required");
        }

        this.reward = reward;
    }

    public void setUnlockedAt(LocalDateTime unlockedAt) {
        if (unlockedAt == null) {
            throw new ValidationException("Unlocked date is required");
        }

        this.unlockedAt = unlockedAt;
    }

    // domain methods

// Redeems the reward unlocked by a specific user.
// It prevents redeeming rewards that are already used, expired,
// not published, not started yet or outside their validity window.

    public void redeem() {
        if (this.status == UserRewardStatus.REDEEMED) { // non si puo' usare due volte lo stesso premio
            throw new ValidationException("Reward has already been redeemed");
        }

        if (this.status == UserRewardStatus.EXPIRED) { // non si puo' usare un premio scaduto
            throw new ValidationException("Expired reward cannot be redeemed");
        }

        if (!this.reward.isCurrentlyValid()) { // metodo di reward NEGATO --> reward NON attivo e/o NON utilizzabile in questa finestra di tempo
            if (this.reward.isExpired()) { // se il motivo e' : metodo di reward --> se data scadenza passata rispetto ad oggi: scaduto
                this.status = UserRewardStatus.EXPIRED; // marcalo come tale
            }

            throw new ValidationException("Reward is not currently valid");
        }

        this.status = UserRewardStatus.REDEEMED; // se tutto ok --> segna come riscattato adesso
        this.redeemedAt = LocalDateTime.now();
    }

    public void markAsExpired() {
        if (this.status != UserRewardStatus.REDEEMED) {
            this.status = UserRewardStatus.EXPIRED;
        }
    }


    @Override
    public String toString() {
        return "UserReward{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "N/A") +
                ", reward=" + (reward != null ? reward.getTitle() : "N/A") +
                ", unlockedAt=" + unlockedAt +
                ", redeemedAt=" + redeemedAt +
                ", status=" + status +
                '}';
    }
}

