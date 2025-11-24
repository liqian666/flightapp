package epam.autotesting.flightbooking.requestsresponses;

import java.time.LocalDate;
import java.util.List;

public class BookingOneWayRequest {
    private String userIdentityCardNumber;
    private String flightNumber;
    private LocalDate departureDate;
    private List<BookingPassengerRequest> passengers;

    public BookingOneWayRequest() {}

    public String getUserIdentityCardNumber() {
        return userIdentityCardNumber;
    }

    public void setUserIdentityCardNumber(String userIdentityCardNumber) {
        this.userIdentityCardNumber = userIdentityCardNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public List<BookingPassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<BookingPassengerRequest> passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "passengerId='" + userIdentityCardNumber + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
