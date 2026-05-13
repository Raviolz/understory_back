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

