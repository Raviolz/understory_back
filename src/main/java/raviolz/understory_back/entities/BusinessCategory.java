package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "business_categories")
public class BusinessCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, unique = true, length = 100)
    private String label;

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String icon;

    public BusinessCategory(String code, String label, String description, String icon) {
        setCode(code);
        setLabel(label);
        this.description = description;
        this.icon = icon;
    }

    public void setCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Business category code is required");
        }
        this.code = code.trim().toUpperCase();
    }

    public void setLabel(String label) {
        if (label == null || label.isBlank()) {
            throw new ValidationException("Business category label is required");
        }
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "BusinessCategory{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
