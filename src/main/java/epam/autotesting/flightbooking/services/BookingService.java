package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.BaggageType;
import epam.autotesting.flightbooking.helper.BookingStatus;
import epam.autotesting.flightbooking.helper.PaymentStatus;
import epam.autotesting.flightbooking.model.*;
import epam.autotesting.flightbooking.repository.BookingRepository;
import epam.autotesting.flightbooking.repository.FlightInfoRepository;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import epam.autotesting.flightbooking.repository.UserRepository;
import epam.autotesting.flightbooking.requestsresponses.BookingPassengerRequest;
import epam.autotesting.flightbooking.requestsresponses.BookingOneWayRequest;
import epam.autotesting.flightbooking.requestsresponses.BookingTwoWayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private BaggageService baggageService;
    @Autowired
    private SeatService seatService;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(BookingOneWayRequest request) {

        String userIdNumber = request.getUserIdentityCardNumber();
        String flightNumber = request.getFlightNumber();

        List<BookingPassengerRequest> requestPassengers = request.getPassengers();
        logger.info("Passengers size {}", requestPassengers.size());
        logger.info("Passengers 1 IDNumber {}", requestPassengers.get(0).getIdentityCardNumber());

        List<Passenger> passengerList = new ArrayList<>();
        List<String> seats = new ArrayList<>();

        logger.info("preparing passenger information to be registered ");
        for(BookingPassengerRequest passenger : requestPassengers) {

            Passenger newPassenger = getPassenger(passenger, flightNumber);
            Passenger savedPassenger = passengerService.savePassenger(newPassenger);
            logger.info("Passenger saved without baggages successfully");


            logger.info("Preparing baggage information for passenger");
            List<Baggage> baggageList = new ArrayList<>();

            List<BaggageType> baggageTypeList = passenger.getBaggageTypes();
            logger.info("Requested Baggages size {} " , baggageTypeList.size());

            //add the baggage
            for(BaggageType baggageTypes : baggageTypeList){
                Baggage newBaggage = new Baggage();
                newBaggage.setPassenger(savedPassenger);
                newBaggage.setBaggageType(baggageTypes);
                baggageList.add(newBaggage);
                logger.info("Baggage added for passenger {} in baggageList", newBaggage.getPassenger().getIdentityCardNumber());
            }
            //save the baggages to the already saved passenger
            logger.info("BaggageList size {} " , baggageList.size());
            logger.info("trying to save baggage information");
            baggageService.saveBaggages(baggageList);
            logger.info("Baggages saved to passenger{}", savedPassenger.getIdentityCardNumber());

            //update passenger with the baggages
            savedPassenger.setBaggages(baggageList);
            passengerService.savePassenger(savedPassenger);
            logger.info("Passenger updated with the baggages {}", savedPassenger.getIdentityCardNumber());


            passengerList.add(newPassenger);
            seats.add(passenger.getSeatNumber());
        }

        logger.info("Processing Booking information");

        Booking booking = new Booking();
        booking.setUserId(userIdNumber);
        booking.setFlightNumber(flightNumber);
        booking.setPassengers(null);
        booking.setSeats(seats);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.UNPAID);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setDepartureDate(request.getDepartureDate());

        if(request instanceof BookingTwoWayRequest){
            booking.setReturnDate(((BookingTwoWayRequest) request).getReturnDate());
            booking.setReturnFlightNumber(((BookingTwoWayRequest) request).getReturnFlightNumber());

        }

        Booking savedbooking = bookingRepository.save(booking);
        logger.info("booking created with flight {} with no passenger", flightNumber);
        logger.info("booking created with booking_id {}", savedbooking.getBookingId());


        savedbooking.setPassengers(passengerList);
        bookingRepository.save(savedbooking);
        logger.info("Booking created with passenger {} ", userIdNumber);

         return savedbooking;
    }

    private static Passenger getPassenger(BookingPassengerRequest passenger, String flightNumber) {
        Passenger newPassenger = new Passenger();

        newPassenger.setFirstName(passenger.getFirstName());
        newPassenger.setLastName(passenger.getLastName());
        newPassenger.setBirthday(passenger.getBirthday());
        newPassenger.setIdentityCardType(passenger.getIdentityCardType());
        newPassenger.setIdentityCardNumber(passenger.getIdentityCardNumber());
        newPassenger.setSeatNumber(passenger.getSeatNumber());
        newPassenger.setFlightNumber(flightNumber);
        newPassenger.setBaggages(null);
        return newPassenger;
    }

    public Optional<Booking> findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public Optional<Booking> confirmBooking(Long bookingId) {
        Optional<Booking> booking = findBookingById(bookingId);
        if (booking.isPresent()) {
            Booking booking1 = booking.get();
            booking1.setBookingStatus(BookingStatus.CONFIRMED);
            booking1.setUpdatedAt(LocalDateTime.now());
            return  Optional.of(bookingRepository.save(booking1));
        }
        return Optional.empty();
    }

    public Optional<Booking> cancelBooking(Long bookingId) {
        Optional<Booking> booking = findBookingById(bookingId);
        if(booking.isPresent()) {
            Booking booking1 = booking.get();
            if (canCancel(booking1)) {
                booking1.setBookingStatus(BookingStatus.CANCELLED);
                if(booking1.getPaymentStatus().equals(PaymentStatus.PAID)) {
                    booking1.setPaymentStatus(PaymentStatus.REFOUND);
                }
                booking1.setSeats(null);
                booking1.setUpdatedAt(LocalDateTime.now());
                return Optional.of(bookingRepository.save(booking1));
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    private boolean canCancel(Booking booking) {
        if (booking.getBookingStatus().equals(BookingStatus.PENDING)) {
            return true;
        }
        else if (booking.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            return false;
        } else if(booking.getBookingStatus().equals(BookingStatus.CANCELLED)) {
            return false;
        }
        return  false;
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

}