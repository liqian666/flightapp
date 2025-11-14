package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;


    public Optional<Passenger> findByPassengerId(Long passengerId) {
        return passengerRepository.findByPassengerId(passengerId);
    }

    public Passenger findByFirstNameAndLastName(String firstName, String lastName) {
        return passengerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Passenger savePassenger(Passenger passenger) {
        passenger.setIdNumber(passenger.getIdNumber());
        return passengerRepository.save(passenger);
    }

}
