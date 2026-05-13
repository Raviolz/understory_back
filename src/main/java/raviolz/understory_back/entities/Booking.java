package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.enums.BookingStatus;
import raviolz.understory_back.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_reward_id", nullable = false, unique = true)
    private UserReward userReward;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private BookingStatus status;

    @Column(length = 1000)
    private String notes;

    @Column(name = "people_count", nullable = false)
    private int peopleCount;

    public Booking(UserReward userReward, LocalDateTime bookingDate, String notes, int peopleCount
    ) {

        setUserReward(userReward);
        setBookingDate(bookingDate);
        this.notes = notes;
        setPeopleCount(peopleCount);
        this.status = BookingStatus.PENDING;
    }

    public void setUserReward(UserReward userReward) {
        if (userReward == null) {
            throw new ValidationException("User reward is required");
        }
        this.userReward = userReward;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        if (bookingDate == null || bookingDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Booking date must be declared and cannot be in the past");
        }
        this.bookingDate = bookingDate;
    }

    public void setPeopleCount(int peopleCount) {
        if (peopleCount <= 0) {
            throw new ValidationException("People count must be greater than zero");
        }
        this.peopleCount = peopleCount;
    }


    public void updateNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userReward=" + (userReward != null ? userReward.getId() : "N/A") +
                ", bookingDate=" + bookingDate +
                ", status=" + status +
                ", notes='" + notes + '\'' +
                ", peopleCount=" + peopleCount +
                '}';
}