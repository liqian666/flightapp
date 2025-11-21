package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    public Optional<Passenger> findByPassengerId(Long passengerId);
    public Optional<Passenger> findByFirstNameAndLastName(String firstName, String lastName);
    public Optional<Passenger> findByIdTypeAndIdNumber(IDType idType, String idNumber);
    public Optional<Passenger> findByIdNumber(String idNumber);
    public Passenger save(Passenger passenger);
}
