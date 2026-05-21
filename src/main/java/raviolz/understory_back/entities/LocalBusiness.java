package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "local_businesses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "address", "city_id"})
        }
)
public class LocalBusiness {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "business_category_id", nullable = false)
    private BusinessCategory businessCategory;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 1500, nullable = false)
    private String description;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private boolean active = false;

    public LocalBusiness(City city, BusinessCategory businessCategory, String name, String address, String description, String websiteUrl, String imageUrl, double longitude, double latitude
    ) {
        setCity(city);
        setBusinessCategory(businessCategory);
        setName(name);
        setAddress(address);
        setDescription(description);
        setWebsiteUrl(websiteUrl);
        setImageUrl(imageUrl);
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

    public void setBusinessCategory(BusinessCategory businessCategory) {
        if (businessCategory == null) {
            throw new ValidationException("Business category is required");
        }

        this.businessCategory = businessCategory;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name is required");
        }

        this.name = name.trim();
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new ValidationException("Address is required");
        }

        this.address = address.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Description is required");
        }

        this.description = description;
    }


    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl != null && !websiteUrl.isBlank()
                ? websiteUrl.trim()
                : null;
    }

    public void setImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new ValidationException("Image URL is required");
        }

        this.imageUrl = imageUrl.trim();
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

    public void publish() {
        this.active = true;
    }

    public void unpublish() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "LocalBusiness{" +
                "id=" + id +
                ", city=" + (city != null ? city.getName() : "N/A") +
                ", businessCategory=" + (businessCategory != null ? businessCategory.getCode() : "N/A") +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", active=" + active +
                '}';
    }
}