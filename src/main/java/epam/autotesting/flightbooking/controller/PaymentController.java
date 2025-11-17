package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.model.Payment;
import epam.autotesting.flightbooking.requestsresponses.ApiResponse;
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
    public ResponseEntity<ApiResponse> processPayment(@RequestBody Payment payment)
    {
        if((payment.getBookingId()!=null)&&(payment.getPassengerId()!=null))
        {
            Optional<Booking> booking = bookingService.findBookingById(payment.getBookingId());
            if(booking.isEmpty()) {
                return ResponseHelper.badRequest(ResponseCodes.BOOKING_NOT_FOUND,"No Booking found with this booking ID",payment.getBookingId());
            }
            Optional<Passenger> passenger = passengerService.findByPassengerId(payment.getPassengerId());
            if(passenger.isEmpty()) {
                return ResponseHelper.badRequest(ResponseCodes.BOOKING_NOT_FOUND,"No Booking found with this passenger ID",payment.getPassengerId());
            }
            Payment paidPayment = paymentService.processPaying(payment);
            return ResponseHelper.success(paidPayment);
        }
        else return ResponseHelper.badRequest(ResponseCodes.NOT_ENOUGH_INFORMATION,"Booking ID or Passenger ID is null",payment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> findByPaymentId(@PathVariable Long paymentId) {
        if (paymentId == null) {
            return ResponseHelper.badRequest(ResponseCodes.PAYMENT_ID_EMPTY, "Payment Id is empty", null);
        }

        return paymentService.findPaymentByPaymentId(paymentId)
                .map(ResponseHelper::success)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.PAYMENT_NOT_FOUND, "Payment not found", paymentId));
    }
}
