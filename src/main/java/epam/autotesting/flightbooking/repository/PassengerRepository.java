package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    public Optional<Passenger> findByPassengerId(Long passengerId);
    public Passenger findByFirstNameAndLastName(String firstName, String lastName);
    public Passenger save(Passenger passenger);
}
