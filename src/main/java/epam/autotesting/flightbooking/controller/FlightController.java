package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.FlightInfo;
import epam.autotesting.flightbooking.requestsresponses.ApiResponse;
import epam.autotesting.flightbooking.requestsresponses.ResponseCodes;
import epam.autotesting.flightbooking.requestsresponses.ResponseHelper;
import epam.autotesting.flightbooking.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.FloatLiteral;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/flights")
public class FlightController {
    @Autowired
    private FlightService flightService;


    @GetMapping("/all")
    public ResponseEntity<List<FlightInfo>> searchAllFlights(){

        List<FlightInfo> allFlights =  flightService.findAllFlights();

        if (!allFlights.isEmpty()) {
            return ResponseEntity.ok(allFlights);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/location")
    public ResponseEntity<ApiResponse> searchFlightsByLocation(@RequestParam String origin,
                                                               @RequestParam String destination,
                                                               @RequestParam(defaultValue = "2025-11-10") String departureDate) {
        if((origin==null)||(destination==null)||(departureDate==null)){
            return ResponseHelper.badRequest(ResponseCodes.NOT_ENOUGH_INFORMATION,
                    "origin or destination or depature data is empty",
                    "origin: "+origin+" destination: "+destination+" depature data: "+departureDate);
        }

        List<FlightInfo> allFlights = flightService.searchFlightsByOriginDestinationDepartureDate(origin, destination, departureDate);
        if (!allFlights.isEmpty()) {
            return ResponseHelper.success(allFlights);
        }
        return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,"flight not found","origin: "+origin+" destination: "+destination+" depature data: "+departureDate);

    }

    @GetMapping("/number")
    public ResponseEntity<ApiResponse> searchFlightsByNumberOfPerson(@RequestParam String origin,
                                                                    @RequestParam String destination,
                                                                    @RequestParam(defaultValue = "2025-11-10") String departureDate,
                                                                    @RequestParam int passengerNumber ){
        if((origin==null)||(destination==null)||(departureDate==null) ||(passengerNumber<=0)){
            return ResponseHelper.badRequest(ResponseCodes.NOT_ENOUGH_INFORMATION,
                    "origin or destination or depature data is empty",
                    "origin: "+origin+" destination: "+destination+" depature data: "+departureDate);
        }

        List<FlightInfo> availableFlights = flightService.searchFlightsByOriginAndDestinationDepartureDateAndPassengerNumber(origin,
                destination, departureDate,passengerNumber);

        if (!availableFlights.isEmpty()) {
            return ResponseHelper.success(availableFlights);
        }
        return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,
                "No available flights found",
                "origin: "+origin+" destination: "+destination+" depature data: "+departureDate);

    }

    @GetMapping("/{flightNumber}/search")
    public ResponseEntity<ApiResponse> searchFlightsByFlightNumber(@PathVariable String flightNumber){
        Optional<FlightInfo> foundFlight = flightService.searchFlightByFlightNumber(flightNumber);
        return foundFlight.map(ResponseHelper::success).orElseGet(() -> ResponseHelper.flightNotFound(flightNumber));
    }

    @DeleteMapping("/{flightNumber}/delete")
    public ResponseEntity<ApiResponse> deleteFlight(@PathVariable String flightNumber){
        Optional<FlightInfo> toBeDeletedFlight = flightService.searchFlightByFlightNumber(flightNumber);
        if(toBeDeletedFlight.isPresent()){
            flightService.deleteFlightByFlightNumber(flightNumber);
            return ResponseHelper.success("Flight" + flightNumber + "deleted successfully" );
        }
        return ResponseHelper.flightNotFound(flightNumber);
    }
}
