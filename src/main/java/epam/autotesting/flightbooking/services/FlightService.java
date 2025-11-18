package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.model.FlightInfo;
import epam.autotesting.flightbooking.repository.FlightInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {
    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    @Autowired
    private FlightInfoRepository flightInfoRepository;

    public List<FlightInfo> findAllFlights() {
        return flightInfoRepository.findAll();
    }

    public List<FlightInfo> searchFlightsByOriginDestinationDepartureDate(String origin,
                                          String destination,
                                          String departureDate) {
        return flightInfoRepository.findByOriginAndDestinationAndDepartureDate(origin, destination, departureDate);
    }

    public List<FlightInfo> searchFlightsByOriginAndDestinationDepartureDateAndPassengerNumber(String origin,
                                                                                               String destination,
                                                                                               String departureDate,
                                                                                               int passengerNumber) {
        List<FlightInfo> allFlights = searchFlightsByOriginDestinationDepartureDate(origin, destination, departureDate);
        logger.info("Number of flights: " + allFlights.size());
        logger.info("passengerNumber: " + passengerNumber);

        List<FlightInfo> availableFlights = allFlights.stream()
                .filter(flightInfo -> flightInfo.getSeatsAvailable() >= passengerNumber)
                .toList();
        logger.info("availableFlights: " + availableFlights.size());
        return availableFlights;

    }



    public List<FlightInfo> searchByOriginAndDestination(String origin, String destination) {
        return flightInfoRepository.findByOriginAndDestination(origin, destination);
    }
}
