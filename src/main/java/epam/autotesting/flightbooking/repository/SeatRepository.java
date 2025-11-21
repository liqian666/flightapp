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

    @Modifying
    @Transactional
    @Query("UPDATE Seat s SET s.isAvailable = false WHERE s.flightNumber = :flightNumber AND s.seatNumber = :seatNumber AND s.isAvailable = true")
    int bookSeat(String flightNumber, String seatNumber);
}
