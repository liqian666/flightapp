package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    public Optional<Payment> findByPaymentId(Long paymentId);
    public void deleteByPaymentId(Long paymentId);
}
