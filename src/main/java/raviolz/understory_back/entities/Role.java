package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false, length = 50)
    private String code;
    @Column(unique = true, nullable = false, length = 100)
    private String label;
    @Column(nullable = false, length = 200)
    private String description;


    public Role(String code, String label, String description) {
        setCode(code);
        setLabel(label);
        setDescription(description);
    }

    public void setCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Role code is required");
        }
        this.code = code.trim().toUpperCase();
    }

    public void setLabel(String label) {
        if (label == null || label.isBlank()) {
            throw new ValidationException("Role label is required");
        }
        this.label = label.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Role description is required");
        }
        this.description = description;
    }


    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
