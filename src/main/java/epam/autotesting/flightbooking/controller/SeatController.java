package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.FlightInfo;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.model.Seat;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import epam.autotesting.flightbooking.repository.SeatRepository;
import epam.autotesting.flightbooking.requestsresponses.ApiResponse;
import epam.autotesting.flightbooking.requestsresponses.ResponseCodes;
import epam.autotesting.flightbooking.requestsresponses.ResponseHelper;
import epam.autotesting.flightbooking.services.FlightService;
import epam.autotesting.flightbooking.services.PassengerService;
import epam.autotesting.flightbooking.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private PassengerService passengerService;


    @GetMapping("/available")
    public ResponseEntity<ApiResponse> findAllAvailableSeats(@RequestParam String flightNumber) {
        if(flightNumber==null){
            return ResponseHelper.badRequest(ResponseCodes.NOT_ENOUGH_INFORMATION,
                    "Flight Number is empty",
                    flightNumber);
        }

        Optional<FlightInfo> flightInfo = flightService.searchFlightByFlightNumber(flightNumber);
        if(flightInfo.isEmpty()){
            return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,
                    "Flight not found",
                    flightNumber);
        }

        List<Seat> availableSeats = seatService.findAllAvailableSeats(flightNumber,true);
        if(!availableSeats.isEmpty()){
            return ResponseHelper.success(availableSeats);
        }
        return ResponseHelper.badRequest(ResponseCodes.NO_AVAILABLE_SEATS,
                "Flight is full",
                flightNumber);
    }

    @PostMapping("/bookseat")
    public ResponseEntity<ApiResponse> bookSeat(@RequestParam String seatNumber, @RequestParam String flightNumber, @RequestParam String passengerIdNumber) {
        if((seatNumber==null)||(flightNumber==null)||(passengerIdNumber==null)){
            return ResponseHelper.badRequest(ResponseCodes.SEAT_NUMBER_IS_EMPTY,
                    "Seat number or Flight number or PassengerId Number is empty",
                    seatNumber);
        }

        Optional<FlightInfo> foundFlight = flightService.searchFlightByFlightNumber(flightNumber);

        if(foundFlight.isEmpty()){
            return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,
                    "Flight not found",
                    " flightNumber: "+ flightNumber);
        }

        Optional<Passenger> foundPassenger = passengerService.findPassengerByIdNumber(passengerIdNumber);
        if(foundPassenger.isEmpty()){
            return ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,
                    "Passenger not found",
                    " passengerIdNumber: "+ passengerIdNumber);
        }


        Seat bookedSeat = seatService.bookSeat(seatNumber, flightNumber, foundPassenger.get());
        if(bookedSeat!=null){
            return ResponseHelper.success(bookedSeat);
        }
        return ResponseHelper.badRequest(ResponseCodes.NO_AVAILABLE_SEATS,
                "Booking Seat failed",
                "seatNumber: "+ seatNumber+" flightNumber: "+ flightNumber);
    }

}
