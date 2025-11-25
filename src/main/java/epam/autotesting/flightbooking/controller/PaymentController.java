package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.model.Payment;
import epam.autotesting.flightbooking.requestsresponses.ApiResponse;
import epam.autotesting.flightbooking.requestsresponses.PaymentRequest;
import epam.autotesting.flightbooking.requestsresponses.ResponseCodes;
import epam.autotesting.flightbooking.requestsresponses.ResponseHelper;
import epam.autotesting.flightbooking.services.BookingService;
import epam.autotesting.flightbooking.services.PassengerService;
import epam.autotesting.flightbooking.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
public class PaymentController
{
    @Autowired
    PaymentService paymentService;
    @Autowired
    BookingService bookingService;
    @Autowired
    PassengerService passengerService;

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse> processPayment(@RequestBody PaymentRequest paymentRequest)
    {
        Long bookingId = paymentRequest.getBookingId();
        if(bookingId == null){
            return  ResponseHelper.badRequest(ResponseCodes.NOT_ENOUGH_INFORMATION,
                    "Booking ID  is null",bookingId);
        }
        Payment paidPayment = paymentService.processPaying(paymentRequest);
        if(paidPayment == null) {
            return ResponseHelper.failedToPay( "Booking ID: " + bookingId);
        }
        return ResponseHelper.success(paidPayment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> findByPaymentId(@PathVariable Long paymentId) {
        return paymentService.findPaymentByPaymentId(paymentId)
                .map(ResponseHelper::success)
                .orElseGet(() -> ResponseHelper.paymentNotFound(paymentId));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> deleteByPaymentId(@PathVariable Long paymentId) {
        Optional<Payment>  toBeDeletedPayment = paymentService.findPaymentByPaymentId(paymentId);
        if(toBeDeletedPayment.isPresent()) {
            paymentService.deletePaymentByPaymentId(paymentId);
            return ResponseHelper.success(toBeDeletedPayment);
        }
        return ResponseHelper.paymentNotFound(paymentId);
    }
}
