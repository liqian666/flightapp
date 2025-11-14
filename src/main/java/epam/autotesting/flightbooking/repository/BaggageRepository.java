package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaggageRepository extends JpaRepository<Baggage, Long> {


    // Find all baggages for a given passenger
    List<Baggage> findByPassenger(Passenger passenger);

}
