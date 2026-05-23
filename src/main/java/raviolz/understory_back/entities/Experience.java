package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.enums.GameType;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "experiences")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "point_of_interest_id", nullable = false)
    private PointOfInterest pointOfInterest;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_game", nullable = false)
    private GameType gameType;

    @Column(name = "hook_text", nullable = false, length = 1000)
    private String hookText;

    @Column(name = "intro_text", nullable = false, length = 3000)
    private String introText;

    @Column(name = "reveal_title", nullable = false, length = 300)
    private String revealTitle;

    @Column(name = "reveal_image_url")
    private String revealImageUrl;

    @Column(name = "reveal_text", nullable = false, length = 3000)
    private String revealText;

    @Column(name = "journal_text", nullable = false, length = 1000)
    private String journalText;

    @Column(name = "xp_reward", nullable = false)
    private int xpReward;

    @Column(nullable = false)
    private int difficulty;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "experience_category_id", nullable = false)
    private ExperienceCategory experienceCategory;

    public Experience(PointOfInterest pointOfInterest, String title, GameType gameType, String hookText,
                      String introText, String revealTitle, String revealImageUrl, String revealText, String journalText,
                      int xpReward, int difficulty, ExperienceCategory experienceCategory) {
        setPointOfInterest(pointOfInterest);
        setTitle(title);
        setGameType(gameType);
        setHookText(hookText);
        setIntroText(introText);
        setRevealTitle(revealTitle);
        setRevealImageUrl(revealImageUrl);
        setRevealText(revealText);
        setJournalText(journalText);
        setXpReward(xpReward);
        setDifficulty(difficulty);
        this.active = false;
        setExperienceCategory(experienceCategory);
    }

    public Experience(PointOfInterest pointOfInterest, String title, GameType gameType, String hookText,
                      String introText, String revealTitle, String revealText, String journalText,
                      int xpReward, int difficulty, ExperienceCategory experienceCategory) {
        setPointOfInterest(pointOfInterest);
        setTitle(title);
        setGameType(gameType);
        setHookText(hookText);
        setIntroText(introText);
        setRevealTitle(revealTitle);
        setRevealText(revealText);
        setJournalText(journalText);
        setXpReward(xpReward);
        setDifficulty(difficulty);
        this.active = false;
        setExperienceCategory(experienceCategory);
    }


    public void setPointOfInterest(PointOfInterest pointOfInterest) {
        if (pointOfInterest == null) {
            throw new ValidationException("Point of interest is required");
        }
        this.pointOfInterest = pointOfInterest;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Experience title is required");
        }
        this.title = title;
    }

    public void setGameType(GameType gameType) {
        if (gameType == null) {
            throw new ValidationException("Game type is required");
        }
        this.gameType = gameType;
    }

    public void setXpReward(int xpReward) {
        if (xpReward <= 0) {
            throw new ValidationException("XP reward must be a positive number");
        }
        this.xpReward = xpReward;
    }

    public void setHookText(String hookText) {
        if (hookText == null || hookText.isBlank()) {
            throw new ValidationException("Hook text is required");
        }
        this.hookText = hookText;
    }

    public void setIntroText(String introText) {
        if (introText == null || introText.isBlank()) {
            throw new ValidationException("Intro text is required");
        }
        this.introText = introText;
    }


    public void setRevealTitle(String revealTitle) {
        if (revealTitle == null || revealTitle.isBlank()) {
            throw new ValidationException("Reveal title is required");
        }
        this.revealTitle = revealTitle;
    }

    public void setRevealImageUrl(String revealImageUrl) {
        if (revealImageUrl == null || revealImageUrl.isBlank()) {
            throw new ValidationException("Reveal image URL is required");
        }

        this.revealImageUrl = revealImageUrl.trim();
    }

    public void setRevealText(String revealText) {
        if (revealText == null || revealText.isBlank()) {
            throw new ValidationException("Reveal text is required");
        }
        this.revealText = revealText;
    }

    public void setJournalText(String journalText) {
        if (journalText == null || journalText.isBlank()) {
            throw new ValidationException("Journal text is required");
        }
        this.journalText = journalText;
    }

    public void setDifficulty(int difficulty) {
        if (difficulty < 1 || difficulty > 5) {
            throw new ValidationException("Difficulty must be between 1 and 5");
        }

        this.difficulty = difficulty;
    }


    public void publish() {
        this.active = true;
    }

    public void unpublish() {
        this.active = false;
    }

    public void setExperienceCategory(ExperienceCategory experienceCategory) {
        if (experienceCategory == null) {
            throw new ValidationException("Experience category is required");
        }
        this.experienceCategory = experienceCategory;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "id=" + id +
                ", pointOfInterest=" + (pointOfInterest != null ? pointOfInterest.getName() : "N/A") +
                ", experienceCategory=" + (experienceCategory != null ? experienceCategory.getCode() : "N/A") +
                ", title='" + title + '\'' +
                ", gameType=" + gameType +
                ", hookText='" + hookText + '\'' +
                ", introText='" + introText + '\'' +
                ", revealTitle='" + revealTitle + '\'' +
                ", revealImageUrl='" + revealImageUrl + '\'' +
                ", revealText='" + revealText + '\'' +
                ", journalText='" + journalText + '\'' +
                ", xpReward=" + xpReward +
                ", difficulty=" + difficulty +
                ", active=" + active +
                '}';
    }
}