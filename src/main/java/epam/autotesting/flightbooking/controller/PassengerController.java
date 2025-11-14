package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.helper.ApiResponse;
import epam.autotesting.flightbooking.helper.ResponseCodes;
import epam.autotesting.flightbooking.helper.ResponseHelper;
import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.requests.PassengerRequest;
import epam.autotesting.flightbooking.services.BaggageService;
import epam.autotesting.flightbooking.services.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private BaggageService baggageservice;


    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    @GetMapping("/passenger")
    public ResponseEntity<Passenger> findPassenger(@RequestParam String firstName, String lastName) {

        Passenger passenger =  passengerService.findByFirstNameAndLastName(firstName,lastName);

        if (passenger != null) {
            return ResponseEntity.ok(passenger);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/id")
    public ResponseEntity<ApiResponse> searchByPassengerId(@RequestParam Long passengerId){

        Optional<Passenger> passenger =  passengerService.findByPassengerId(passengerId);
        if(passenger.isPresent()){
            return ResponseHelper.success(ResponseCodes.SUCCESS,"find the passenger by passengerId",passenger);
        }

        return ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,"cannot find the passenger by passengerId",passengerId);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> savePassenger(@RequestBody PassengerRequest passengerRequest) {
        Passenger passenger = getPassenger(passengerRequest);

        Passenger savedPassenger = passengerService.savePassenger(passenger);
        return ResponseHelper.success(ResponseCodes.SUCCESS,"Passenger Registered successfully", savedPassenger);

    }

    private static Passenger getPassenger(PassengerRequest passengerRequest) {
        Passenger passenger = new  Passenger();
        passenger.setFirstName(passengerRequest.getFirstName());
        passenger.setLastName(passengerRequest.getLastName());
        passenger.setBirthday(passengerRequest.getBirthday());
        passenger.setBaggages(null);
        passenger.setIdType(passengerRequest.getIdType());
        passenger.setIdNumber(passengerRequest.getIdNumber());
        passenger.setSeatNumber(passengerRequest.getSeatNumber());
        passenger.setFlightNumber(passengerRequest.getFlightNumber());
        return passenger;
    }
}
