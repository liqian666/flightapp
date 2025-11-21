package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.model.Seat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends CrudRepository<Seat,Integer> {
    public List<Seat> findByFlightNumberAndIsAvailable(String flightNumber, boolean available);
    public Optional<Seat> findBySeatNumberAndFlightNumber(String seatNumber, String flightNumber);
}