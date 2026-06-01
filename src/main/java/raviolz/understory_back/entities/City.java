package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(nullable = false)
    private boolean active;

    public City(String name, String country, double longitude, double latitude, String description, String coverImageUrl) {
        setName(name);
        setCountry(country);
        setLongitude(longitude);
        setLatitude(latitude);
        setDescription(description);
        setCoverImageUrl(coverImageUrl);
        this.active = false;
    }

    public City(String name, String country, Double longitude, Double latitude, String description) {
        setName(name);
        setCountry(country);

        if (longitude == null) {
            throw new ValidationException("Longitude is required");
        }

        if (latitude == null) {
            throw new ValidationException("Latitude is required");
        }

        setLongitude(longitude);
        setLatitude(latitude);
        setDescription(description);

        this.active = false;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("City name is required");
        }
        this.name = name.trim();
    }

    public void setCountry(String country) {
        if (country == null || country.isBlank()) {
            throw new ValidationException("Country is required");
        }
        this.country = country.trim();
    }

    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new ValidationException("Longitude must be between -180 and 180 degrees");
        }
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new ValidationException("Latitude must be between -90 and 90 degrees");
        }
        this.latitude = latitude;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Description is required");
        }
        this.description = description;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl != null && !coverImageUrl.isBlank()
                ? coverImageUrl.trim()
                : null;
    }


    public void publish() {
        this.active = true;
    }

    public void unpublish() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", description='" + description + '\'' +
                ", coverImageUrl='" + coverImageUrl + '\'' +
                ", active=" + active +
                '}';
    }
}
