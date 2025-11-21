package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.model.FlightInfo;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.model.Seat;
import epam.autotesting.flightbooking.repository.FlightInfoRepository;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import epam.autotesting.flightbooking.repository.SeatRepository;
import io.micrometer.observation.GlobalObservationConvention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private FlightInfoRepository flightInfoRepository;
    @Autowired
    private PassengerRepository passengerRepository;

    private static final Logger logger = LoggerFactory.getLogger(SeatService.class);

    public List<Seat> findAllAvailableSeats(String flightNumber, boolean available) {
        return seatRepository.findByFlightNumberAndIsAvailable(flightNumber,available);
    }

    public Seat bookSeat(String seatNumber, String flightNumber, Passenger passenger) {
           Optional<Seat> foundSeat = seatRepository.findBySeatNumberAndFlightNumber(seatNumber, flightNumber);

           if(foundSeat.isPresent()){
               Seat seat = foundSeat.get();

               logger.info("Found seatNumber: {}", seat.getSeatNumber());
               logger.info("Found flightNumber: {}",seat.getFlightNumber());
               if(seat.isAvailable()){
                   seat.setAvailable(false);
                   seatRepository.save(seat);

                   passenger.setSeatNumber(seat.getSeatNumber());
                   passengerRepository.save(passenger);

                   //update the flight with seat information also
                   return seat;
               }
               return null;
           }
          return null;
    }

    public boolean isSeatAvailable(String flightNumber, String seatNumber) {
        Optional<Seat> foundSeat = seatRepository.findBySeatNumberAndFlightNumber(seatNumber,flightNumber);
        if(foundSeat.isPresent()){
            Seat seat = foundSeat.get();
            return seat.isAvailable();
        }
        return false;
    }
}
