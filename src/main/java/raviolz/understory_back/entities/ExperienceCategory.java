package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;

@Getter

@NoArgsConstructor
@Entity
@Table(name = "experience_categories")
public class ExperienceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true, length = 50)
    private String label;
    @Column(nullable = false, unique = true, length = 50)
    private String code;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, length = 50)
    private String icon;
    @Column(nullable = false)
    private String color;


    public ExperienceCategory(String label, String code, String description, String icon, String color) {
        setLabel(label);
        setCode(code);
        setDescription(description);
        setIcon(icon);
        setColor(color);
    }

    public void setLabel(String label) {
        if (label == null || label.isBlank()) {
            throw new ValidationException("Label is required");
        }
        this.label = label.trim();
    }

    public void setCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Code is required");
        }
        this.code = code.trim().toUpperCase();
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Description is required");
        }
        this.description = description.trim();
    }

    public void setIcon(String icon) {
        if (icon == null || icon.isBlank()) {
            throw new ValidationException("Icon  is required");
        }
        this.icon = icon.trim();
    }

    public void setColor(String color) {
        if (color == null || color.isBlank()) {
            throw new ValidationException("Color  is required");
        }
        this.color = color.trim();
    }

    @Override
    public String toString() {
        return "ExperienceCategory{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
