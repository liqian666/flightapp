package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.requestsresponses.*;
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
@RequestMapping("api/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;


    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    @GetMapping("/name")
    public ResponseEntity<ApiResponse> findPassenger(@RequestParam String firstName, String lastName) {
        if(firstName==null || lastName==null) {
            ResponseHelper.badRequest(ResponseCodes.NOT_ENOUGH_INFORMATION,"firstName or lastName is Empty","firstName:"+firstName+" lastName:"+lastName);
        }

        return passengerService.findByFirstNameAndLastName(firstName,lastName)
                .map(this::getPassengerResponseEntity)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,"Passenger not found", null));
    }

    @GetMapping("/id")
    public ResponseEntity<ApiResponse> searchByPassengerId(@RequestParam Long passengerId){

        return passengerService.findByPassengerId(passengerId)
                .map(this::getPassengerResponseEntity)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,"Passenger not found", passengerId));

    }

    @GetMapping("/passengerIdType")
    public ResponseEntity<ApiResponse> findUserById(@RequestParam String passengerIdType, @RequestParam String passengerIdNumber) {
        if ((passengerIdType==null) || (passengerIdNumber == null)) {
            return ResponseHelper.badRequest(ResponseCodes.USER_ID_EMPTY, "User IDType or User IDNumber is empty", "passengerIdType: "+passengerIdType+" passengerIdNumber: "+passengerIdNumber);
        }

        IDType idTypeEnum;
        try {
            idTypeEnum = IDType.valueOf(passengerIdType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseHelper.badRequest(ResponseCodes.USER_ID_EMPTY, "User IDTYPE should be one fo the following : PASSPORT,\n" +
                    "    NATIONAL_ID,\n" +
                    "    DRIVER_LICENSE", "UserIdType: "+passengerIdType+" UserIdNumber: "+passengerIdNumber);
        }

        return passengerService.findPassengerByIdTypeAndIdNumber(idTypeEnum, passengerIdNumber)
                .map(this::getPassengerResponseEntity)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND, "Passenger not found", "passengerIdType: "+passengerIdType+" passengerIdNumber: "+passengerIdNumber));
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

    private ResponseEntity<ApiResponse> getPassengerResponseEntity(Passenger foundPassenger) {
        PassengerRequest passengerRequest = new PassengerRequest();
        passengerRequest.setBirthday(foundPassenger.getBirthday());
        passengerRequest.setFirstName(foundPassenger.getFirstName());
        passengerRequest.setLastName(foundPassenger.getLastName());
        passengerRequest.setIdType(foundPassenger.getIdType());
        passengerRequest.setIdNumber(foundPassenger.getIdNumber());
        passengerRequest.setSeatNumber(foundPassenger.getSeatNumber());
        passengerRequest.setFlightNumber(foundPassenger.getFlightNumber());
        return ResponseHelper.success(passengerRequest);
    }
}
