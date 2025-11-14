package epam.autotesting.flightbooking.repository;


import epam.autotesting.flightbooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    public Booking save(Booking booking);
    public List<Booking> findAll();
    public Booking findById(long id);
    public void deleteById(long id);
}
