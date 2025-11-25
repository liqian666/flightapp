package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    public Optional<Passenger> findByPassengerId(Long passengerId);
    public Optional<Passenger> findByFirstNameAndLastNameAndFlightNumber(String firstName, String lastName, String flightNumber);
    public Optional<Passenger> findByIdentityCardNumberAndFlightNumber(String idNumber, String flightNumber);
    public Optional<Passenger> findByIdentityCardTypeAndIdentityCardNumberAndFlightNumber(IDType idType, String idNumber, String flightNumber);
    public Passenger save(Passenger passenger);
}
