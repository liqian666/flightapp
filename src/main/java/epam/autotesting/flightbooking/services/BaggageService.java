package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.repository.BaggageRepository;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public List<Baggage> saveBaggages(List<Baggage> toBeSavedBaggages) {

        //get the already existing baggages of the passenger
        if(toBeSavedBaggages.isEmpty()){
            logger.debug("BaggageService.saveBaggages: toBeSavedBaggages.isEmpty()");
            return null;
        }
        Passenger passenger = toBeSavedBaggages.get(0).getPassenger();
        logger.info("save baggages to passenger {}", passenger.getIdentityCardNumber());

        //if the passagener has already some baggages
//        List<Baggage> baggagesList = passenger.getBaggages();
//        if(baggagesList.isEmpty()){
//            logger.info(" this passenger doesn't have other saved baggages ");
//        }

        List<Baggage> baggagesList = new ArrayList<>();
        for (Baggage baggage : toBeSavedBaggages) {
            logger.info("baggage weight is {}", baggage.getBaggageType());
            Baggage savedBaggage = baggageRepository.save(baggage);
            baggagesList.add(savedBaggage);
        }

        logger.info("baggages saved {}", baggagesList.size());

        for(Baggage baggage : baggagesList){
            logger.info("baggage weight is {}", baggage.getBaggageType());
        }

        //udpate the passenger with new baggages
//        passenger.getBaggages().addAll(baggages);
//        passenger.setBaggages(baggages);
//        passengerRepository.save(passenger);
        return baggagesList;

    }

    public List<Baggage> findBaggageByPassengerIdAndFlightNumber(String identityCardNumber,String flightNumber) {
        Optional<Passenger> passenger = passengerRepository.findByIdentityCardNumberAndFlightNumber(identityCardNumber,flightNumber);
        if(passenger.isPresent()){
            logger.info("passenger ID {}",passenger.get().getIdentityCardNumber());
            return baggageRepository.findByPassenger(passenger.get());
        }
        return null;
    }

    @Transactional
    public void deleteBaggageById(Long id) {
         baggageRepository.deleteById(id);
    }

    @Transactional
    public void deleteBaggageByPassenger(Passenger passenger) {
        baggageRepository.deleteByPassenger(passenger);
    }
}
