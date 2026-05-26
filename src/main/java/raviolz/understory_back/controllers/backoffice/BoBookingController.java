package raviolz.understory_back.controllers.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import raviolz.understory_back.enums.BookingStatus;
import raviolz.understory_back.payloads.responses.BookingResponseDTO;
import raviolz.understory_back.services.BookingService;

import java.util.UUID;

@RestController
@RequestMapping("/backoffice/bookings")
public class BoBookingController {

    private final BookingService bookingService;

    public BoBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public Page<BookingResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "bookingDate") String sortBy) {
        return bookingService.findAll(page, size, sortBy)
                .map(BookingResponseDTO::fromEntity);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDTO findById(@PathVariable UUID bookingId) {
        return BookingResponseDTO.fromEntity(
                bookingService.findById(bookingId)
        );
    }

    @GetMapping("/status/{status}")
    public Page<BookingResponseDTO> findByStatus(@PathVariable BookingStatus status,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "bookingDate") String sortBy) {
        return bookingService.findByStatus(status, page, size, sortBy)
                .map(BookingResponseDTO::fromEntity);
    }

    @GetMapping("/business/{businessId}")
    public Page<BookingResponseDTO> findByBusiness(@PathVariable UUID businessId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "bookingDate") String sortBy) {
        return bookingService.findByBusiness(businessId, page, size, sortBy)
                .map(BookingResponseDTO::fromEntity);
    }

    @PatchMapping("/{bookingId}/confirm")
    public BookingResponseDTO confirm(@PathVariable UUID bookingId) {
        return BookingResponseDTO.fromEntity(
                bookingService.confirm(bookingId)
        );
    }

    @PatchMapping("/{bookingId}/reject")
    public BookingResponseDTO reject(@PathVariable UUID bookingId) {
        return BookingResponseDTO.fromEntity(
                bookingService.reject(bookingId)
        );
    }
}