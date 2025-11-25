package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.requestsresponses.*;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.services.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;


    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    @GetMapping("/name")
    public ResponseEntity<ApiResponse> findPassenger(@RequestParam String firstName,
                                                     @RequestParam String lastName,
                                                     @RequestParam String flightNumber) {

        return passengerService.findByFirstNameAndLastNameAndFlightNumber(firstName,lastName,flightNumber)
                .map(this::getPassengerResponseEntity)
                .orElseGet(() -> ResponseHelper.passengerNotFound("First: " + firstName + " LastName: " + lastName));
    }

    @GetMapping("/passengerIdType")
    public ResponseEntity<ApiResponse> findPassengerById(@RequestParam IDType identityCardType,
                                                         @RequestParam String identityCardNumber,
                                                         @RequestParam String flightNumber) {

        return passengerService.findByIdentityCardTypeAndIdentityCardNumberAndFlightNumber(identityCardType, identityCardNumber, flightNumber)
                .map(this::getPassengerResponseEntity)
                .orElseGet(() -> ResponseHelper.passengerNotFound("passengerIdType: "+identityCardType +
                        " passengerIdNumber: "+identityCardNumber));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> savePassenger(@RequestBody PassengerRequest passengerRequest) {
        Passenger passenger = getPassenger(passengerRequest);

        Passenger savedPassenger = passengerService.savePassenger(passenger);
        return ResponseHelper.success(savedPassenger);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deletePassenger(@RequestParam String identityCardNumber,
                                                       @RequestParam String flightNumber) {
        Optional<Passenger> toBeDeletedPassenger = passengerService.findPassengerBIdentityCardNumberAndFlightNumber(identityCardNumber,flightNumber);
        if(toBeDeletedPassenger.isPresent()) {
            toBeDeletedPassenger.ifPresent(passenger -> passengerService.deletePassenger(passenger));
            return  ResponseHelper.success("Passenger: " + identityCardNumber + " has been deleted");
        }

        return ResponseHelper.passengerNotFound("passengerIdNumber: "+identityCardNumber +
                " FlightNumber: "+flightNumber);
    }

    private static Passenger getPassenger(PassengerRequest passengerRequest) {
        Passenger passenger = new  Passenger();
        passenger.setFirstName(passengerRequest.getFirstName());
        passenger.setLastName(passengerRequest.getLastName());
        passenger.setBirthday(passengerRequest.getBirthday());
        passenger.setBaggages(null);
        passenger.setIdentityCardType(passengerRequest.getIdentityCardType());
        passenger.setIdentityCardNumber(passengerRequest.getIdentityCardNumber());
        passenger.setSeatNumber(passengerRequest.getSeatNumber());
        passenger.setFlightNumber(passengerRequest.getFlightNumber());
        return passenger;
    }

    private ResponseEntity<ApiResponse> getPassengerResponseEntity(Passenger foundPassenger) {
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setBirthday(foundPassenger.getBirthday());
        passengerRequest.setFirstName(foundPassenger.getFirstName());
        passengerRequest.setLastName(foundPassenger.getLastName());
        passengerRequest.setIdentityCardType(foundPassenger.getIdentityCardType());
        passengerRequest.setIdentityCardNumber(foundPassenger.getIdentityCardNumber());
        passengerRequest.setSeatNumber(foundPassenger.getSeatNumber());
        passengerRequest.setFlightNumber(foundPassenger.getFlightNumber());
        return ResponseHelper.success(passengerRequest);
    }
}
