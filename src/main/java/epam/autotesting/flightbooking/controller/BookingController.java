package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.requests.BookingRequest;
import epam.autotesting.flightbooking.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/save")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        if (request.getUserId().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return bookingService.createBooking(request);
    }

    @PostMapping("/{bookingId}/confirm")
    public Booking confirmBooking(@PathVariable String bookingId) {
        return bookingService.confirmBooking(Long.parseLong (bookingId));
    }

    @PostMapping("/{bookingId}/cancel")
    public Booking cancelBooking(@PathVariable String bookingId) {
        return bookingService.cancelBooking(Long.parseLong (bookingId));
    }


}
