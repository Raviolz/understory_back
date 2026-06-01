package raviolz.understory_back.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;

    @Column(nullable = false)
    private int xp;

    @Column(nullable = false)
    private int level;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(String username, String name, String surname, String email, String password, Role role) {
        setUsername(username);
        setName(name);
        setSurname(surname);
        setEmail(email);
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password is required");
        }
        this.password = password;
        this.avatarUrl = "https://ui-avatars.com/api/?name=" + name + "+" + surname; // momentaneo in attesa di avatar base a tema
        this.xp = 0;
        this.level = 1;
        if (role == null) {
            throw new ValidationException("Role is required");
        }
        this.role = role;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }
        if (username.length() < 3) {
            throw new ValidationException("Username must contain at least 3 characters");
        }
        this.username = username.trim();
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required");
        }
        this.name = name.trim();

    }

    public void setSurname(String surname) {
        if (surname == null || surname.isBlank()) {
            throw new ValidationException("Surname is required");
        }
        this.surname = surname.trim();

    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Invalid email format");
        }
        this.email = email.trim().toLowerCase();
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new ValidationException("Role is required");
        }

        this.role = role;
    }

    public void setAvatarUrl(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            throw new ValidationException("Avatar URL is required");
        }

        this.avatarUrl = avatarUrl.trim();
    }


    // domain methods

    private void recalculateLevel() {
        this.level = (this.xp / 100) + 1; // NB: E' un int quindi ad esempio 98/100 = 0 senza parte decimale +1 --> lev 1
    }

    public void addXp(int amount) {
        if (amount <= 0) {
            throw new ValidationException("XP amount must be positive");
        }
        this.xp += amount;
        recalculateLevel();
    }

    // security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getCode()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", xp=" + xp +
                ", level=" + level +
                ", roleCode='" + (role != null ? role.getCode() : "N/A") + '\'' +
                '}';
    }
}
