package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.enums.UploadSubmissionStatus;
import raviolz.understory_back.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "user_upload_submissions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "experience_id"})})
public class UserUploadSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private UploadSubmissionStatus status;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    public UserUploadSubmission(User user, Experience experience, String imageUrl) {
        setUser(user);
        setExperience(experience);
        setImageUrl(imageUrl);
        this.status = UploadSubmissionStatus.SUBMITTED; // V1: parte da submitted.. l' approvazione automatica la metto nel service .. in caso AI validation piu' avanti
        this.submittedAt = LocalDateTime.now();
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

    public void setImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new ValidationException("Image URL is required");
        }
        this.imageUrl = imageUrl.trim();


    }

    public void setStatus(UploadSubmissionStatus status) {
        if (status == null) {
            throw new ValidationException("Upload submission status is required");
        }

        this.status = status;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        if (submittedAt == null) {
            throw new ValidationException("Submitted At is required");
        }
        this.submittedAt = submittedAt;
    }

    // domain methods

    public void approve() {
        this.status = UploadSubmissionStatus.APPROVED;
    }

    public void reject() {
        this.status = UploadSubmissionStatus.REJECTED;
    }

    public void resubmitImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new ValidationException("Image URL is required");
        }

        this.imageUrl = imageUrl.trim();
        this.status = UploadSubmissionStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "UserUploadSubmission{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "N/A") +
                ", experience=" + (experience != null ? experience.getTitle() : "N/A") +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                ", submittedAt=" + submittedAt +
                '}';
    }
}