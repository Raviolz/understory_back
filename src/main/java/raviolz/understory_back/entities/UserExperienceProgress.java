package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.enums.ProgressStatus;
import raviolz.understory_back.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "user_experience_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "experience_id"})
        }
)
public class UserExperienceProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "user_note", length = 1000)
    private String userNote;

    @Column(name = "xp_awarded", nullable = false)
    private boolean xpAwarded;

    public UserExperienceProgress(User user, Experience experience) {
        setUser(user);
        setExperience(experience);
        setStatus(ProgressStatus.IN_PROGRESS);
        this.startedAt = LocalDateTime.now();
        this.completedAt = null;
        this.userNote = null;
        this.xpAwarded = false;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new ValidationException("User is required");
        }

        this.user = user;
    }

    public void setExperience(Experience experience) {
        if (experience == null) {
            throw new ValidationException("Experience is required");
        }

        this.experience = experience;
    }

    public void setStatus(ProgressStatus status) {
        if (status == null) {
            throw new ValidationException("Progress status is required");
        }

        this.status = status;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        if (startedAt == null) {
            throw new ValidationException("Started date is required");
        }

        this.startedAt = startedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public void setXpAwarded(boolean xpAwarded) {
        this.xpAwarded = xpAwarded;
    }


    public void complete() {
        if (this.status == ProgressStatus.COMPLETED) {
            return;
        }

        this.status = ProgressStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public boolean isCompleted() {
        return this.status == ProgressStatus.COMPLETED;
    }

    public void markXpAwarded() {
        if (!isCompleted()) {
            throw new ValidationException("Cannot award XP before completing the experience");
        }
        if (this.xpAwarded) {
            return;
        }
        this.xpAwarded = true;
    }

    public void updateUserNote(String userNote) {
        this.userNote = userNote != null && !userNote.isBlank()
                ? userNote.trim()
                : null;
    }


    @Override
    public String toString() {
        return "UserExperienceProgress{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "N/A") +
                ", experience=" + (experience != null ? experience.getTitle() : "N/A") +
                ", status=" + status +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", userNote='" + userNote + '\'' +
                ", xpAwarded=" + xpAwarded +
                '}';
    }
}