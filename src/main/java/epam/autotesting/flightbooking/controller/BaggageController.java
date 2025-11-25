package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.helper.BaggageType;
import epam.autotesting.flightbooking.requestsresponses.*;
import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.services.BaggageService;
import epam.autotesting.flightbooking.services.PassengerService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Baggage Management", description = "Operations related to baggage")
public class BaggageController {
    @Autowired
    private BaggageService baggageService;
    @Autowired
    private PassengerService passengerService;

    private static final Logger logger = LoggerFactory.getLogger(BaggageController.class);

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBaggages(@RequestParam List<BaggageType> baggageTypes,
                                                   @RequestParam String identityCardNumber,
                                                   @RequestParam String flightNumber) {

        Optional<Passenger> passenger = passengerService.findPassengerBIdentityCardNumberAndFlightNumber(identityCardNumber, flightNumber);
        if(passenger.isPresent()){
            List<Baggage> baggageList = new ArrayList();
            for(BaggageType baggageType : baggageTypes){
                logger.info("Adding baggage with  {} ", baggageType);
                Baggage baggage = new Baggage();
                baggage.setBaggageType(baggageType);
                baggage.setPassenger(passenger.get());

                baggageList.add(baggage);
            }

            List<Baggage> savedBaggageList = baggageService.saveBaggages(baggageList);
            List<BaggageType>  baggageTypeList = new ArrayList<>();
            BaggageResponse baggageResponse = new BaggageResponse();
            baggageResponse.setIdentityCardNumber(identityCardNumber);
            for (Baggage savedBaggage : savedBaggageList) {
                logger.info("Saving baggage with id {} ", savedBaggage.getId());
                baggageTypeList.add(savedBaggage.getBaggageType());
            }
            baggageResponse.setBaggageTypeList(baggageTypeList);
            return ResponseHelper.success(baggageResponse);
        }
        else
            return ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,"Passenger not found", null);

    }

    @GetMapping("/find")
    public ResponseEntity<ApiResponse> searchByPassengerId(@RequestParam String identityCardNumber,@RequestParam String flightNumber) {

        List<Baggage> baggages = baggageService.findBaggageByPassengerIdAndFlightNumber(identityCardNumber,flightNumber);
        if (baggages!=null) {
            logger.info("baggages found {} ",baggages);
            BaggageResponse baggageResponse = new BaggageResponse();
            baggageResponse.setIdentityCardNumber(identityCardNumber);
            List<BaggageType> baggageTypes = new ArrayList<>();
            for (Baggage baggage : baggages) {
                baggageTypes.add(baggage.getBaggageType());

            }
            baggageResponse.setBaggageTypeList(baggageTypes);
            return ResponseHelper.success(baggageResponse);
        }
        return ResponseHelper.baggageNotFound("Passenger identity card number : " + identityCardNumber);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteBaggages(@RequestParam String identityCardNumber,
                                                      @RequestParam String flightNumber,
                                                      @RequestParam BaggageType baggageType) {
        List<Baggage> baggages = baggageService.findBaggageByPassengerIdAndFlightNumber(identityCardNumber,flightNumber);
        if (baggages==null) {
            return ResponseHelper.baggageNotFound("passenger identity card number : " + identityCardNumber +
            "flight number: " + flightNumber +
            "baggage type : " + baggageType);
        }
        Optional<Baggage> baggageToDelete = baggages.stream()
                .filter(b -> b.getBaggageType().equals(baggageType))
                .findFirst();

        baggageToDelete.ifPresent(b -> baggageService.deleteBaggageById(b.getId()));

        BaggageResponse baggageResponse = new BaggageResponse();
        List<BaggageType> baggageTypes = new ArrayList<>();
        baggageTypes.add(baggageType);
        baggageResponse.setIdentityCardNumber(identityCardNumber);
        baggageResponse.setBaggageTypeList(baggageTypes);

        return ResponseHelper.success(baggageResponse);
    }
}