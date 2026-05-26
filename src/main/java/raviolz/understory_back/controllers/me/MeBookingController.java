package raviolz.understory_back.controllers.me;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.payloads.BookingDTO;
import raviolz.understory_back.payloads.responses.BookingResponseDTO;
import raviolz.understory_back.services.BookingService;

import java.util.UUID;

@RestController
@RequestMapping("/me/bookings")
public class MeBookingController {

    private final BookingService bookingService;

    public MeBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDTO createBooking(@AuthenticationPrincipal User currentUser,
                                            @RequestBody @Valid BookingDTO body) {
        return BookingResponseDTO.fromEntity(
                bookingService.createForUser(currentUser.getId(), body)
        );
    }

    @GetMapping
    public Page<BookingResponseDTO> findMyBookings(@AuthenticationPrincipal User currentUser,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "bookingDate") String sortBy) {
        return bookingService.findByUser(currentUser.getId(), page, size, sortBy)
                .map(BookingResponseDTO::fromEntity);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDTO findMyBookingById(@AuthenticationPrincipal User currentUser,
                                                @PathVariable UUID bookingId) {
        return BookingResponseDTO.fromEntity(
                bookingService.findByIdForUser(currentUser.getId(), bookingId)
        );
    }

    @PatchMapping("/{bookingId}/cancel")
    public BookingResponseDTO cancelMyBooking(@AuthenticationPrincipal User currentUser,
                                              @PathVariable UUID bookingId) {
        return BookingResponseDTO.fromEntity(
                bookingService.cancelForUser(currentUser.getId(), bookingId)
        );
    }
}