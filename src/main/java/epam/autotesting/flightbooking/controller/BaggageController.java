package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.helper.ApiResponse;
import epam.autotesting.flightbooking.helper.ResponseCodes;
import epam.autotesting.flightbooking.helper.ResponseHelper;
import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.repository.BaggageRepository;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import epam.autotesting.flightbooking.requests.BaggageResponse;
import epam.autotesting.flightbooking.services.BaggageService;
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
    private PassengerRepository passengerRepository;

    private static final Logger logger = LoggerFactory.getLogger(BaggageController.class);

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> saveBaggage(@RequestParam Double  weight, @RequestParam Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId).orElse(null);
        if(passenger!=null){
            logger.info("Adding baggage for passenger "+passenger.getPassengerId());
            baggageService.saveBaggage(weight,passenger);
            return ResponseHelper.success(weight);
        }
        else
            return ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,"Passenger not found", null);

    }

    @GetMapping("/find/passengerId")
    public ResponseEntity<ApiResponse> searchByPassengerId(@RequestParam Long passengerId) {

        List<Baggage> baggages = baggageService.findBaggageByPassengerId(passengerId);
        if (!baggages.isEmpty()) {
            logger.info("baggages found {} ",baggages);
            List<BaggageResponse>  baggageResponses = new ArrayList<>();
            for (Baggage baggage : baggages) {
                BaggageResponse baggageResponse = new BaggageResponse();
                baggageResponse.setPassengerId(passengerId);
                baggageResponse.setWeight(baggage.getWeight());
                baggageResponses.add(baggageResponse);
            }
            return ResponseHelper.success(baggageResponses);

        }
        logger.info("did not found baggages ");
        return ResponseHelper.badRequest(ResponseCodes.BAGGAGE_NOT_FOUND, "Passenger doesn't have baggage", passengerId);

    }
}
