package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.BaggageType;
import epam.autotesting.flightbooking.helper.BookingStatus;
import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.helper.PaymentStatus;
import epam.autotesting.flightbooking.model.Baggage;
import epam.autotesting.flightbooking.model.Booking;
import epam.autotesting.flightbooking.model.Passenger;
import epam.autotesting.flightbooking.repository.BookingRepository;
import epam.autotesting.flightbooking.repository.FlightInfoRepository;
import epam.autotesting.flightbooking.repository.PassengerRepository;
import epam.autotesting.flightbooking.repository.UserRepository;
import epam.autotesting.flightbooking.requestsresponses.BookingOneWayRequest;
import epam.autotesting.flightbooking.requestsresponses.BookingPassengerRequest;
import epam.autotesting.flightbooking.requestsresponses.BookingTwoWayRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private FlightInfoRepository flightInfoRepository;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PassengerService passengerService;
    @Mock
    private BaggageService baggageService;
    @Mock
    private SeatService seatService;

    @InjectMocks
    private BookingService bookingService;

    private BookingPassengerRequest passengerRequest;

    @BeforeEach
    void setUp() {
        passengerRequest = new BookingPassengerRequest();
        passengerRequest.setFirstName("John");
        passengerRequest.setLastName("Doe");
        passengerRequest.setBirthday(LocalDate.of(1990, 1, 1));
        passengerRequest.setIdentityCardType(IDType.PASSPORT);
        passengerRequest.setIdentityCardNumber("P123456");
        passengerRequest.setSeatNumber("12A");
        passengerRequest.setBaggageTypes(List.of(BaggageType.CABIN_BAG, BaggageType.CHECKED_LUGGAGE));
    }

    // --- getAllBookings ---

    @Test
    void getAllBookings_returnsAllBookingsFromRepository() {
        Booking b1 = new Booking();
        Booking b2 = new Booking();
        when(bookingRepository.findAll()).thenReturn(List.of(b1, b2));

        List<Booking> result = bookingService.getAllBookings();

        assertThat(result).hasSize(2).containsExactly(b1, b2);
    }

    // --- createBooking (one-way) ---

    @Test
    void createBooking_oneWay_savesBookingWithCorrectFields() {
        BookingOneWayRequest request = new BookingOneWayRequest();
        request.setUserIdentityCardNumber("U999");
        request.setFlightNumber("AB123");
        request.setDepartureDate(LocalDate.of(2026, 5, 1));
        request.setPassengers(List.of(passengerRequest));

        Passenger savedPassenger = new Passenger();
        savedPassenger.setIdentityCardNumber("P123456");
        when(passengerService.savePassenger(any(Passenger.class))).thenReturn(savedPassenger);

        Booking persistedBooking = new Booking();
        persistedBooking.setBookingId(1L);
        when(bookingRepository.save(any(Booking.class))).thenReturn(persistedBooking);

        Booking result = bookingService.createBooking(request);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository, times(2)).save(bookingCaptor.capture());

        Booking firstSave = bookingCaptor.getAllValues().get(0);
        assertThat(firstSave.getUserId()).isEqualTo("U999");
        assertThat(firstSave.getFlightNumber()).isEqualTo("AB123");
        assertThat(firstSave.getDepartureDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(firstSave.getBookingStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(firstSave.getPaymentStatus()).isEqualTo(PaymentStatus.UNPAID);
        assertThat(firstSave.getReturnFlightNumber()).isNull();
        assertThat(firstSave.getReturnDate()).isNull();
        assertThat(result).isNotNull();
    }

    @Test
    void createBooking_oneWay_savesPassengerAndBaggages() {
        BookingOneWayRequest request = new BookingOneWayRequest();
        request.setUserIdentityCardNumber("U999");
        request.setFlightNumber("AB123");
        request.setDepartureDate(LocalDate.of(2026, 5, 1));
        request.setPassengers(List.of(passengerRequest));

        Passenger savedPassenger = new Passenger();
        savedPassenger.setIdentityCardNumber("P123456");
        when(passengerService.savePassenger(any(Passenger.class))).thenReturn(savedPassenger);

        Booking persistedBooking = new Booking();
        persistedBooking.setBookingId(1L);
        when(bookingRepository.save(any(Booking.class))).thenReturn(persistedBooking);

        bookingService.createBooking(request);

        // Passenger is saved twice: once without baggages, once with baggages attached
        verify(passengerService, times(2)).savePassenger(any(Passenger.class));
        // Baggages are saved once (the list)
        verify(baggageService, times(1)).saveBaggages(anyList());
    }

    @Test
    void createBooking_oneWay_seatNumbersCollectedFromPassengers() {
        BookingPassengerRequest p2 = new BookingPassengerRequest();
        p2.setFirstName("Jane");
        p2.setLastName("Smith");
        p2.setBirthday(LocalDate.of(1995, 6, 15));
        p2.setIdentityCardType(IDType.NATIONAL_ID);
        p2.setIdentityCardNumber("N654321");
        p2.setSeatNumber("14B");
        p2.setBaggageTypes(List.of(BaggageType.BACKPACK));

        BookingOneWayRequest request = new BookingOneWayRequest();
        request.setUserIdentityCardNumber("U999");
        request.setFlightNumber("AB123");
        request.setDepartureDate(LocalDate.of(2026, 5, 1));
        request.setPassengers(List.of(passengerRequest, p2));

        Passenger savedP1 = new Passenger();
        savedP1.setIdentityCardNumber("P123456");
        Passenger savedP2 = new Passenger();
        savedP2.setIdentityCardNumber("N654321");
        when(passengerService.savePassenger(any(Passenger.class)))
                .thenReturn(savedP1).thenReturn(savedP1).thenReturn(savedP2).thenReturn(savedP2);

        Booking persistedBooking = new Booking();
        persistedBooking.setBookingId(1L);
        when(bookingRepository.save(any(Booking.class))).thenReturn(persistedBooking);

        bookingService.createBooking(request);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository, times(2)).save(captor.capture());
        List<String> seats = captor.getAllValues().get(0).getSeats();
        assertThat(seats).containsExactlyInAnyOrder("12A", "14B");
    }

    // --- createBooking (two-way) ---

    @Test
    void createBooking_twoWay_setsReturnDateAndReturnFlightNumber() {
        BookingTwoWayRequest request = new BookingTwoWayRequest();
        request.setUserIdentityCardNumber("U999");
        request.setFlightNumber("AB123");
        request.setDepartureDate(LocalDate.of(2026, 5, 1));
        request.setReturnDate(LocalDate.of(2026, 5, 15));
        request.setReturnFlightNumber("CD789");
        request.setPassengers(List.of(passengerRequest));

        Passenger savedPassenger = new Passenger();
        savedPassenger.setIdentityCardNumber("P123456");
        when(passengerService.savePassenger(any(Passenger.class))).thenReturn(savedPassenger);

        Booking persistedBooking = new Booking();
        persistedBooking.setBookingId(2L);
        when(bookingRepository.save(any(Booking.class))).thenReturn(persistedBooking);

        bookingService.createBooking(request);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository, times(2)).save(captor.capture());

        Booking savedBooking = captor.getAllValues().get(0);
        assertThat(savedBooking.getReturnDate()).isEqualTo(LocalDate.of(2026, 5, 15));
        assertThat(savedBooking.getReturnFlightNumber()).isEqualTo("CD789");
    }

    // --- findBookingById ---

    @Test
    void findBookingById_returnsBookingWhenFound() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        when(bookingRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(booking));

        Optional<Booking> result = bookingService.findBookingById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getBookingId()).isEqualTo(1L);
    }

    @Test
    void findBookingById_returnsEmptyWhenNotFound() {
        when(bookingRepository.findById(Long.valueOf(99L))).thenReturn(Optional.<Booking>empty());

        Optional<Booking> result = bookingService.findBookingById(99L);

        assertThat(result).isEmpty();
    }

    // --- confirmBooking ---

    @Test
    void confirmBooking_setsStatusToConfirmed() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus(BookingStatus.PENDING);
        when(bookingRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Optional<Booking> result = bookingService.confirmBooking(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getBookingStatus()).isEqualTo(BookingStatus.CONFIRMED);
        verify(bookingRepository).save(booking);
    }

    @Test
    void confirmBooking_returnsEmptyWhenBookingNotFound() {
        when(bookingRepository.findById(Long.valueOf(99L))).thenReturn(Optional.<Booking>empty());

        Optional<Booking> result = bookingService.confirmBooking(99L);

        assertThat(result).isEmpty();
        verify(bookingRepository, never()).save(any());
    }

    // --- cancelBooking ---

    @Test
    void cancelBooking_pendingBooking_setsCancelled() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.UNPAID);
        when(bookingRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Optional<Booking> result = bookingService.cancelBooking(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(result.get().getSeats()).isNull();
    }

    @Test
    void cancelBooking_pendingAndPaid_setsRefound() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PAID);
        when(bookingRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Optional<Booking> result = bookingService.cancelBooking(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getPaymentStatus()).isEqualTo(PaymentStatus.REFOUND);
    }

    @Test
    void cancelBooking_confirmedBooking_returnsEmpty() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        when(bookingRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(booking));

        Optional<Booking> result = bookingService.cancelBooking(1L);

        assertThat(result).isEmpty();
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void cancelBooking_alreadyCancelledBooking_returnsEmpty() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setBookingStatus(BookingStatus.CANCELLED);
        when(bookingRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(booking));

        Optional<Booking> result = bookingService.cancelBooking(1L);

        assertThat(result).isEmpty();
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void cancelBooking_returnsEmptyWhenBookingNotFound() {
        when(bookingRepository.findById(Long.valueOf(99L))).thenReturn(Optional.<Booking>empty());

        Optional<Booking> result = bookingService.cancelBooking(99L);

        assertThat(result).isEmpty();
    }

    // --- deleteBooking ---

    @Test
    void deleteBooking_deletesPassengersAndBooking() {
        Passenger p1 = new Passenger();
        Passenger p2 = new Passenger();
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setPassengers(List.of(p1, p2));
        when(bookingRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(1L);

        verify(passengerService).deletePassenger(p1);
        verify(passengerService).deletePassenger(p2);
        verify(bookingRepository).deleteById(Long.valueOf(1L));
    }

    @Test
    void deleteBooking_whenBookingNotFound_stillCallsDeleteById() {
        when(bookingRepository.findById(Long.valueOf(99L))).thenReturn(Optional.<Booking>empty());

        bookingService.deleteBooking(99L);

        verify(passengerService, never()).deletePassenger(any());
        verify(bookingRepository).deleteById(Long.valueOf(99L));
    }
}
