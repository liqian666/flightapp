package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.PaymentStatus;
import epam.autotesting.flightbooking.model.Payment;
import epam.autotesting.flightbooking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public Payment processPaying(Payment payment){
        Long paymentId = payment.getPaymentId();
        if(paymentId!=null){
            Optional<Payment> oldPayment = findPaymentByPaymentId(paymentId);
            oldPayment.ifPresent(value -> value.setPaymentStatus(PaymentStatus.PAID));
        }
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findPaymentByPaymentId(Long paymentId){
       return paymentRepository.findByPaymentId(paymentId);
    }
}
