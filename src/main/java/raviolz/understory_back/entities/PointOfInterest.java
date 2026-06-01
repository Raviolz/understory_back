package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "points_of_interest")
public class PointOfInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(nullable = false)
    private String name;

    @Column(name = "short_description", length = 1000, nullable = false)
    private String shortDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;


    @Column(nullable = false)
    private boolean active;


    public PointOfInterest(City city, String name, String shortDescription,
                           double longitude, double latitude) {
        setCity(city);
        setName(name);
        setShortDescription(shortDescription);
        setLongitude(longitude);
        setLatitude(latitude);
        this.active = false;
    }

    public void setCity(City city) {
        if (city == null) {
            throw new ValidationException("City is required");
        }
        this.city = city;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Point of interest name is required");
        }
        this.name = name.trim();
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

    public void setShortDescription(String shortDescription) {
        if (shortDescription == null || shortDescription.isBlank()) {
            throw new ValidationException("A short description is required");
        }
        this.shortDescription = shortDescription.trim();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl != null && !imageUrl.isBlank()
                ? imageUrl.trim()
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
        return "PointOfInterest{" +
                "id=" + id +
                ", city=" + (city != null ? city.getName() : "N/A") +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", active=" + active +
                '}';
    }
}