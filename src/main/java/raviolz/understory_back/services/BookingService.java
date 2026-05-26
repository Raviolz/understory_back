package raviolz.understory_back.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raviolz.understory_back.entities.Booking;
import raviolz.understory_back.entities.UserReward;
import raviolz.understory_back.enums.BookingStatus;
import raviolz.understory_back.enums.UserRewardStatus;
import raviolz.understory_back.exceptions.NotFoundException;
import raviolz.understory_back.exceptions.ValidationException;
import raviolz.understory_back.payloads.BookingDTO;
import raviolz.understory_back.repositories.BookingRepository;

import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRewardService userRewardService;

    public BookingService(BookingRepository bookingRepository,
                          UserRewardService userRewardService) {
        this.bookingRepository = bookingRepository;
        this.userRewardService = userRewardService;
    }

    public Booking createForUser(UUID userId, BookingDTO body) {
        UserReward userReward = userRewardService.findById(body.userRewardId());

        if (!userReward.getUser().getId().equals(userId)) {
            throw new ValidationException("You cannot book another user's reward");
        }

        if (userReward.getStatus() != UserRewardStatus.UNLOCKED) {
            throw new ValidationException("Only unlocked rewards can be booked");
        }

        if (!userReward.getReward().isCurrentlyValid()) {
            userReward = userRewardService.expireIfRewardExpired(userReward.getId());
            throw new ValidationException("Reward is not currently valid");
        }

        if (bookingRepository.existsByUserRewardId(userReward.getId())) {
            throw new ValidationException("A booking already exists for this reward");
        }

        Booking booking = new Booking(
                userReward,
                body.bookingDate(),
                body.notes(),
                body.peopleCount()
        );

        return bookingRepository.save(booking);
    }

    public Page<Booking> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findAll(pageable);
    }

    public Booking findById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
    }

    public Page<Booking> findByUser(UUID userId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findByUserRewardUserId(userId, pageable);
    }

    public Page<Booking> findByStatus(BookingStatus status, int page, int size, String sortBy) {
        if (status == null) {
            throw new ValidationException("Booking status is required");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findByStatus(status, pageable);
    }

    public Page<Booking> findByBusiness(UUID businessId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findByUserRewardRewardBusinessId(businessId, pageable);
    }

    public Booking findByIdForUser(UUID userId, UUID bookingId) {
        Booking found = findById(bookingId);

        if (!found.getUserReward().getUser().getId().equals(userId)) {
            throw new ValidationException("You cannot view another user's booking");
        }

        return found;
    }

    @Transactional
    public Booking confirm(UUID bookingId) {
        Booking found = findById(bookingId);

        found.confirm();

        found.getUserReward().redeem();

        return bookingRepository.save(found);
    }

    public Booking reject(UUID bookingId) {
        Booking found = findById(bookingId);

        found.reject();

        return bookingRepository.save(found);
    }

    public Booking cancelForUser(UUID userId, UUID bookingId) {
        Booking found = findByIdForUser(userId, bookingId);

        found.cancel();

        return bookingRepository.save(found);
    }
}