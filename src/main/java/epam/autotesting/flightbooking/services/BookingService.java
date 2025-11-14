package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.BookingStatus;
import epam.autotesting.flightbooking.helper.PaymentStatus;
import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.model.FlightInfo;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.model.UserInfo;
import epam.autotesting.flightbooking.repository.BookingRepository;
import epam.autotesting.flightbooking.repository.FlightInfoRepository;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import epam.autotesting.flightbooking.repository.UserRepository;
import epam.autotesting.flightbooking.requests.BookingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FlightInfoRepository flightInfoRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PassengerService passengerService;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public ResponseEntity<Booking> createBooking(BookingRequest request) {
        /* To-do Validate flight and seat availability */

        String userId = request.getUserId();
        UserInfo userInfo = userRepository.findByUserId(userId);
        if (userInfo == null) {
            return ResponseEntity.badRequest().build();
        }
        String flightNumber = request.getFlightNumber();
        FlightInfo flightInfo = flightInfoRepository.findByFlightNumber(flightNumber);
        if (flightInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Passenger> passengers = request.getPassengers();
        logger.error("Passengers {}", passengers);

        List<String> seats = new ArrayList<>();

        for(Passenger passenger : passengers) {

            passengerService.savePassenger(passenger);
            seats.add(passenger.getSeatNumber());
        }


        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setFlightNumber(flightInfo.getFlightNumber());
        booking.setPassengers(null);
        booking.setSeats(seats);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.UNPAID);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        Booking savedbooking = bookingRepository.save(booking);
        logger.info("booking created with flight {} with no passenger", flightNumber);
        logger.info("booking created with booking_id {}", savedbooking.getBookingId());


        savedbooking.setPassengers(passengers);
        bookingRepository.save(savedbooking);
        logger.info("Booking created with passenger {} ", userId);

         return ResponseEntity.status(HttpStatus.CREATED).body(savedbooking);
    }

    public Booking confirmBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking != null) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            booking.setPaymentStatus(PaymentStatus.PAID);
            booking.setUpdatedAt(LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (canCancel(booking)) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            if(booking.getPaymentStatus().equals(PaymentStatus.PAID)) {
                booking.setPaymentStatus(PaymentStatus.REFOUND);
            }
            booking.setSeats(null);
            booking.setUpdatedAt(LocalDateTime.now());
            return bookingRepository.save(booking);
        } else {
            throw new IllegalStateException("Booking cannot be cancelled.");
        }
    }

    private boolean canCancel(Booking booking) {
        if (booking.getBookingStatus().equals(BookingStatus.PENDING)) {
            return true;
        }
        else if (booking.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            return true;
        } else if(booking.getBookingStatus().equals(BookingStatus.CANCELLED)) {
            return false;
        }
        return  false;
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

}
