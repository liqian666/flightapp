package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.requestsresponses.*;
import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> createBooking(@RequestBody BookingRequest request) {
        if (request.getUserId()==null) {
            return ResponseHelper.badRequest(ResponseCodes.USER_ID_EMPTY,"Please fill in the User ID",null);
        }
        logger.debug("Creating booking");
         Booking savedBooking = bookingService.createBooking(request);
        logger.debug("Booking Created Successfully");
        if(savedBooking!=null) {
            BookingResponse bookingResponse = new BookingResponse();
            List<String> passengerIDNumbers = new ArrayList<>();
            bookingResponse.setBookingStatus(savedBooking.getBookingStatus());
//            bookingResponse.setPassengers(savedBooking.getPassengers());
            for(Passenger passenger : savedBooking.getPassengers()) {
                passengerIDNumbers.add(passenger.getIdNumber());
            }
            bookingResponse.setPassengersIdNumbers(passengerIDNumbers);
            bookingResponse.setFlightNumber(savedBooking.getFlightNumber());
            bookingResponse.setUserId(savedBooking.getUserId());
            bookingResponse.setSeats(savedBooking.getSeats());
            bookingResponse.setPaymentStatus(savedBooking.getPaymentStatus());
            bookingResponse.setCreatedAt(savedBooking.getCreatedAt());
            bookingResponse.setUpdatedAt(savedBooking.getUpdatedAt());

            return ResponseHelper.success(bookingResponse);
        }
        return ResponseHelper.badRequest(ResponseCodes.BOOKING_FAILED,"Booking failed", savedBooking);
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<ApiResponse> confirmBooking(@PathVariable Long bookingId) {
        if(bookingId==null){
            return ResponseHelper.badRequest(ResponseCodes.BOOKING_ID_EMPTY,"Booking Id is empty",null);
        }

        return bookingService.confirmBooking(bookingId)
                .map(ResponseHelper::success)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.BOOKING_NOT_FOUND, "Booking not found", null));

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse> findBookingById(@PathVariable Long bookingId) {
        if(bookingId==null){
            return ResponseHelper.badRequest(ResponseCodes.BOOKING_ID_EMPTY,"Booking Id is empty",null);
        }

        return bookingService.findBookingById(bookingId)
                .map(ResponseHelper::success)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.BOOKING_NOT_FOUND, "Booking not found", null));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long bookingId) {
        if(bookingId==null){
            return ResponseHelper.badRequest(ResponseCodes.BOOKING_ID_EMPTY,"Booking Id is empty",null);
        }

        return bookingService.cancelBooking(bookingId)
                .map(ResponseHelper::success)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.BOOKING_NOT_FOUND, "Booking not found", null));
    }
}
