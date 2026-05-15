package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.enums.RewardType;
import raviolz.understory_back.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private LocalBusiness business;

    @ManyToOne
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1500)
    private String description;

    @Column(name = "discount_code", length = 80)
    private String discountCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false, length = 50)
    private RewardType rewardType;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @Column(nullable = false)
    private boolean active = false;

    public Reward(LocalBusiness business, Experience experience, String title, String description, String discountCode, RewardType rewardType, LocalDate validFrom, LocalDate validUntil
    ) {
        setBusiness(business);
        setExperience(experience);
        setTitle(title);
        setDescription(description);
        setDiscountCode(discountCode);
        setRewardType(rewardType);
        setValidityPeriod(validFrom, validUntil); // interconnessi inutile separarli per controlli
        this.active = false;
    }

    public void setBusiness(LocalBusiness business) {
        if (business == null) {
            throw new ValidationException("Business is required");
        }

        this.business = business;
    }

    public void setExperience(Experience experience) {
        if (experience == null) {
            throw new ValidationException("Experience is required");
        }

        this.experience = experience;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Reward title is required");
        }

        this.title = title.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Reward description is required");
        }

        this.description = description.trim();
    }

    public void setDiscountCode(String discountCode) { // opzionale non deve lanciare eccezione se null
        this.discountCode = discountCode != null && !discountCode.isBlank()
                ? discountCode.trim()
                : null;
    }

    public void setRewardType(RewardType rewardType) {
        if (rewardType == null) {
            throw new ValidationException("Reward type is required");
        }

        this.rewardType = rewardType;
    }

    public void setValidityPeriod(LocalDate validFrom, LocalDate validUntil) { // controllo che from sia prima di until
        if (validFrom == null) {
            throw new ValidationException("Valid from date is required");
        }

        if (validUntil == null) {
            throw new ValidationException("Valid until date is required");
        }

        if (validUntil.isBefore(validFrom)) {
            throw new ValidationException("Valid until date cannot be before valid from date");
        }

        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public void publish() {
        this.active = true;
    }

    public void unpublish() {
        this.active = false;
    }

// domain methods

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.validUntil);
    }

    public boolean isCurrentlyValid() { // controllo che sia pubblicato (attivo) e che sia utilizzabile in termini di finestra di tempo in cui e' possibile utilizzarlo rispetto ad oggi
        LocalDate today = LocalDate.now(); // reward valido come struttura ma non utilizzabile oggi

        return this.active && // attivo = true
                !today.isBefore(this.validFrom) && // oggi NON è prima della data di inizio
                !today.isAfter(this.validUntil); // oggi NON e' dopo della data di fine
    }


    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", business=" + (business != null ? business.getName() : "N/A") +
                ", experience=" + (experience != null ? experience.getTitle() : "N/A") +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", discountCode='" + discountCode + '\'' +
                ", rewardType=" + rewardType +
                ", validFrom=" + validFrom +
                ", validUntil=" + validUntil +
                ", active=" + active +
                '}';
    }
}