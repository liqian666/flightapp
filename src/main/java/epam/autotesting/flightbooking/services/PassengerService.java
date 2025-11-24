package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.IDType;
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

    public Optional<Passenger> findByFirstNameAndLastName(String firstName, String lastName) {
        return passengerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Passenger savePassenger(Passenger passenger) {
        passenger.setIdentityCardNumber(passenger.getIdentityCardNumber());
        return passengerRepository.save(passenger);
    }

    public Optional<Passenger> findPassengerByIdTypeAndIdNumber(IDType idType, String idNumber) {
        return passengerRepository.findByIdentityCardTypeAndIdentityCardNumber(idType,idNumber);
    }

//    public Optional<Passenger> findPassengerByIdNumber(String idNumber) {
//        return passengerRepository.findByIdentityCardNumber(idNumber);
//    }

    public Optional<Passenger> findPassengerBIdentityCardNumberAndFlightNumber(String identityCardNumber,
                                                                               String flightNumber) {
        return passengerRepository.findByIdentityCardNumberAndFlightNumber(identityCardNumber,flightNumber);
    }

}
