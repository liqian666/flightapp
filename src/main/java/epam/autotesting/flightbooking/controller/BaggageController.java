package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.requestsresponses.*;
import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.services.BaggageService;
import epam.autotesting.flightbooking.services.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/baggage")
public class BaggageController {
    @Autowired
    private BaggageService baggageService;
    @Autowired
    private PassengerService passengerService;

    private static final Logger logger = LoggerFactory.getLogger(BaggageController.class);

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBaggages(@RequestBody List<Double> weights, @RequestParam Long passengerId) {

        if(passengerId==null){
            return ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,"PassengerId is empty",  null);
        }

        Optional<Passenger> passenger = passengerService.findByPassengerId(passengerId);
        if(passenger.isPresent()){
            List<Baggage> baggageList = new ArrayList();
            for(Double weight : weights){
                logger.info("Adding baggage with weight {} ", weight);
                Baggage baggage = new Baggage();
                baggage.setWeight(weight);
                baggage.setPassenger(passenger.get());

                baggageList.add(baggage);
            }

            List<Baggage> savedBaggageList = baggageService.saveBaggages(baggageList);
            List<Double>  baggageWeights = new ArrayList<>();
            BaggageResponse baggageResponse = new BaggageResponse();
            baggageResponse.setPassengerId(passengerId);
            for (Baggage SavedBaggage : savedBaggageList) {
                logger.info("Saving baggage with id {} ", SavedBaggage.getId());
                baggageWeights.add(SavedBaggage.getWeight());
            }
            baggageResponse.setWeight(baggageWeights);
            return ResponseHelper.success(baggageResponse);
        }
        else
            return ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,"Passenger not found", null);

    }

    @GetMapping("/find/passengerId")
    public ResponseEntity<ApiResponse> searchByPassengerId(@RequestParam Long passengerId) {

        List<Baggage> baggages = baggageService.findBaggageByPassengerId(passengerId);
        if (!baggages.isEmpty()) {
            logger.info("baggages found {} ",baggages);
            BaggageResponse baggageResponse = new BaggageResponse();
            baggageResponse.setPassengerId(passengerId);
            List<Double> baggageWeights = new ArrayList<>();
            for (Baggage baggage : baggages) {
                baggageWeights.add(baggage.getWeight());

            }
            baggageResponse.setWeight(baggageWeights);
            return ResponseHelper.success(baggageResponse);

        }
        logger.info("did not found baggages ");
        return ResponseHelper.badRequest(ResponseCodes.BAGGAGE_NOT_FOUND, "Passenger doesn't have baggage", passengerId);

    }
}
