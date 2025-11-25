package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.requestsresponses.*;
import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.services.BookingService;
import epam.autotesting.flightbooking.services.FlightService;
import epam.autotesting.flightbooking.services.UserService;
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
    @Autowired
    UserService userService;
    @Autowired
    FlightService flightService;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/oneway")
    public ResponseEntity<ApiResponse> oneWayBooking(@RequestBody BookingOneWayRequest request) {
        if(userNotFound(request.getUserIdentityCardNumber())) {
            return ResponseHelper.badRequest(ResponseCodes.USER_NOT_FOUND,
                    "User not found with this UserID",request.getFlightNumber());
        }

        if(flightNotFound(request.getFlightNumber())) {
            return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,
                    "Flight not found with this FlightNumber",request.getFlightNumber());
        }

         Booking savedBooking = bookingService.createBooking(request);

        if(savedBooking!=null) {
            return getBookingResponseEntity(savedBooking, new BookingResponse());
        }
        return ResponseHelper.badRequest(ResponseCodes.BOOKING_FAILED,"Booking failed", request);

    }

    @PostMapping("/twoway")
    public ResponseEntity<ApiResponse> twoWayBooking(@RequestBody BookingTwoWayRequest request) {

        if(userNotFound(request.getUserIdentityCardNumber())) {
            return ResponseHelper.badRequest(ResponseCodes.USER_NOT_FOUND,
                    "User not found with this UserID",request.getFlightNumber());
        }

        if(flightNotFound(request.getFlightNumber())) {
            return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,
                    "Flight not found with this FlightNumber",request.getFlightNumber());
        }

        if(flightNotFound(request.getReturnFlightNumber())) {
            return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,
                    "Return Flight not found with this FlightNumber",request.getReturnFlightNumber());
        }

        Booking savedBooking1 = bookingService.createBooking(request);

        BookingOneWayRequest request1 = new BookingOneWayRequest();
        request1.setUserIdentityCardNumber(request.getUserIdentityCardNumber());
        request1.setPassengers(request.getPassengers());
        request1.setFlightNumber(request.getReturnFlightNumber());
        request1.setDepartureDate(request.getReturnDate());

        Booking savedBooking2 = bookingService.createBooking(request1);

        if((savedBooking1!=null)&&(savedBooking2!=null)) {
            return getBookingResponseEntity(savedBooking1, new BookingTwoWayResponse());
        }
        return ResponseHelper.badRequest(ResponseCodes.BOOKING_FAILED,"Booking failed",
                request);

    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<ApiResponse> confirmBooking(@PathVariable Long bookingId) {
        if(bookingId==null){
            return ResponseHelper.badRequest(ResponseCodes.BOOKING_ID_EMPTY,"Booking Id is empty",null);
        }

        return bookingService.confirmBooking(bookingId)
                .map(booking -> getBookingResponseEntity(booking, new BookingResponse()))
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.CONFIRM_BOOKING_FAILED, "Failed to confirm the booking", null));

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse> findBookingById(@PathVariable Long bookingId) {
        if(bookingId==null){
            return ResponseHelper.badRequest(ResponseCodes.BOOKING_ID_EMPTY,"Booking Id is empty",null);
        }

        return bookingService.findBookingById(bookingId)
                .map(booking -> getBookingResponseEntity(booking, new BookingResponse()))
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.BOOKING_NOT_FOUND, "Booking not found", null));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse> cancelBooking(@PathVariable Long bookingId) {

        return bookingService.cancelBooking(bookingId)
                .map(booking -> getBookingResponseEntity(booking, new BookingResponse()))
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.CANCEL_BOOKING_FAILED, "Booking cannot be cancelled", null));

    }

    @DeleteMapping("/{bookingId}/delete")
    public ResponseEntity<ApiResponse> deleteBooking(@PathVariable Long bookingId) {
        Optional<Booking> toBeDeletedBooking = bookingService.findBookingById(bookingId);
        if(toBeDeletedBooking.isPresent()) {
            bookingService.deleteBooking(bookingId);
            return  ResponseHelper.success("Booking Id: " + bookingId + " has been deleted");
        }
        else return ResponseHelper.bookingNotFound("Booking Id: " + bookingId);
    }
    private boolean userNotFound(String userIdNumber) {
        return userService.findUserByUserIdNumber(userIdNumber).isEmpty();
    }

    private boolean flightNotFound(String flightNumber) {
        return flightService.searchFlightByFlightNumber(flightNumber).isEmpty();
    }

    private ResponseEntity<ApiResponse> getBookingResponseEntity(Booking savedBooking, BookingResponse bookingResponse) {
        List<String> passengerIDNumbers = new ArrayList<>();
        bookingResponse.setBookingStatus(savedBooking.getBookingStatus());
        for(Passenger passenger : savedBooking.getPassengers()) {
            passengerIDNumbers.add(passenger.getIdentityCardNumber());
        }
        bookingResponse.setPassengersIdNumbers(passengerIDNumbers);
        bookingResponse.setFlightNumber(savedBooking.getFlightNumber());
        bookingResponse.setUserIdentityCardNumber(savedBooking.getUserId());
        bookingResponse.setSeats(savedBooking.getSeats());
        bookingResponse.setPaymentStatus(savedBooking.getPaymentStatus());
        bookingResponse.setCreatedAt(savedBooking.getCreatedAt());
        bookingResponse.setUpdatedAt(savedBooking.getUpdatedAt());

        if (bookingResponse instanceof BookingTwoWayResponse) {
            ((BookingTwoWayResponse) bookingResponse).setReturnData(savedBooking.getReturnDate());
            ((BookingTwoWayResponse) bookingResponse).setReturnFlightNumber(savedBooking.getReturnFlightNumber());
        }

        return ResponseHelper.success(bookingResponse);
    }
}
