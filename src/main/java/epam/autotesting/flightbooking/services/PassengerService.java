package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    BaggageService baggageService;


    public Optional<Passenger> findByPassengerId(Long passengerId) {
        return passengerRepository.findByPassengerId(passengerId);
    }

    public Optional<Passenger> findByFirstNameAndLastNameAndFlightNumber(String firstName, String lastName, String flightNumber) {
        return passengerRepository.findByFirstNameAndLastNameAndFlightNumber(firstName, lastName, flightNumber);
    }

    public Passenger savePassenger(Passenger passenger) {
        passenger.setIdentityCardNumber(passenger.getIdentityCardNumber());
        return passengerRepository.save(passenger);
    }

    public Optional<Passenger> findPassengerBIdentityCardNumberAndFlightNumber(String identityCardNumber,
                                                                               String flightNumber) {
        return passengerRepository.findByIdentityCardNumberAndFlightNumber(identityCardNumber,flightNumber);
    }

    public Optional<Passenger> findByIdentityCardTypeAndIdentityCardNumberAndFlightNumber(IDType identityCardType,
                                                                                          String identityCardNumber,
                                                                                          String flightNumber) {
        return passengerRepository.findByIdentityCardTypeAndIdentityCardNumberAndFlightNumber(identityCardType,identityCardNumber,flightNumber);
    }

    @Transactional
    public void deletePassenger(Passenger passenger) {
        //delete all the baggages belong to the passenger first
        baggageService.deleteBaggageByPassenger(passenger);
        //then delete the passenger
        passengerRepository.delete(passenger);
    }

}
