package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.BookingStatus;
import epam.autotesting.flightbooking.helper.PaymentStatus;
import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.model.Payment;
import epam.autotesting.flightbooking.repository.BookingRepository;
import epam.autotesting.flightbooking.repository.PaymentRepository;
import epam.autotesting.flightbooking.requestsresponses.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    BookingRepository bookingRepository;

    public Payment processPaying(PaymentRequest paymentRequest){
        Long bookingId = paymentRequest.getBookingId();
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        Payment payment = new Payment();
        if(booking !=null){
            PaymentStatus paidStatus = booking.getPaymentStatus();
            if(paidStatus == PaymentStatus.UNPAID){
                booking.setPaymentStatus(PaymentStatus.PAID);
                booking.setBookingStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);

                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setTransactionDate(LocalDateTime.now());
                payment.setBookingId(bookingId);
                payment.setPaymentMethod(paymentRequest.getPaymentMethod());
                payment.setAmount(paymentRequest.getAmount());
                return paymentRepository.save(payment);

            }
        }
        return null;
    }

    public Optional<Payment> findPaymentByPaymentId(Long paymentId){
       return paymentRepository.findByPaymentId(paymentId);
    }
}
