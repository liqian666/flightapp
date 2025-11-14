package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.model.FlightInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightInfoRepository extends JpaRepository<FlightInfo, Long> {
    List<FlightInfo> findAll();
    List<FlightInfo> findByOriginAndDestinationAndDepartureDate(String origin,
                                                                String destination,
                                                                String departureDate);

    List<FlightInfo> findByOriginAndDestination(String origin, String destination);
    FlightInfo findByFlightNumber(String flightNumber);


}
