package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.FlightInfo;
import epam.autotesting.flightbooking.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/flights")
public class FlightController {
    @Autowired
    private FlightService flightService;


    @GetMapping("/all")
    public ResponseEntity<List<FlightInfo>> searchAllFlights(){

        List<FlightInfo> allFlights =  flightService.findAllFlights();

        if (allFlights != null) {
            return ResponseEntity.ok(allFlights);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/location")
    public ResponseEntity<List<FlightInfo>> searchFlightsByLocation(@RequestParam String origin,
                                          @RequestParam String destination,
                                          @RequestParam(defaultValue = "2025-11-10") String departureDate) {
        List<FlightInfo> allFlights = flightService.searchFlightsByOriginDestinationDepartureDate(origin, destination, departureDate);
        if (allFlights != null) {
            return ResponseEntity.ok(allFlights);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/number")
    public ResponseEntity<List<FlightInfo>> searchFlightsByNumberOfPerson(@RequestParam String origin,
                                                                    @RequestParam String destination,
                                                                    @RequestParam(defaultValue = "2025-11-10") String departureDate,
                                                                    @RequestParam int passengerNumber ){
        List<FlightInfo> availableFlights = flightService.searchFlightsByOriginAndDestinationDepartureDateAndPassengerNumber(origin, destination, departureDate,passengerNumber);

        if (availableFlights != null) {
            return ResponseEntity.ok(availableFlights);
        }
        return ResponseEntity.notFound().build();
    }
}
