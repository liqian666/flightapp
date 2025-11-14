package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.controller.BaggageController;
import epam.autotesting.flightbooking.helper.ResponseCodes;
import epam.autotesting.flightbooking.helper.ResponseHelper;
import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.repository.BaggageRepository;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BaggageService {
    @Autowired
    private BaggageRepository baggageRepository;
    @Autowired
    private PassengerRepository passengerRepository;

    private static final Logger logger = LoggerFactory.getLogger(BaggageService.class);

    public Baggage saveBaggage(Double weight, Passenger passenger) {

        //get the already exist baggages of the passenger
        List<Baggage> baggages = passenger.getBaggages();

        //add the new baggage
        Baggage baggage = new Baggage();
        baggage.setWeight(weight);
        baggage.setPassenger(passenger);
        Baggage savedBaggage = baggageRepository.save(baggage);

        //update the passenger with the baggages
        baggages.add(savedBaggage);
        passenger.setBaggages(baggages);
        passengerRepository.save(passenger);
        return savedBaggage;
    }

    public List<Baggage> findBaggageByPassengerId(Long passengerId) {
        Optional<Passenger> passenger = passengerRepository.findByPassengerId(passengerId);
        if(passenger.isPresent()){
            logger.info("passenger ID {}",passenger.get().getPassengerId());
            return baggageRepository.findByPassenger(passenger.get());
        }
        return null;
    }


}
