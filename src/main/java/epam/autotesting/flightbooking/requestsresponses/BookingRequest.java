package epam.autotesting.flightbooking.requestsresponses;

import epam.autotesting.flightbooking.model.Passenger;

import java.util.List;

public class BookingRequest {
    private String userIdNumber;
    private String flightNumber;
    private List<BookingPassengerRequest> passengers;

    public BookingRequest() {}

    public String getUserIdNumber() {
        return userIdNumber;
    }

    public void setUserIdNumber(String userIdNumber) {
        this.userIdNumber = userIdNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
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
                "passengerId='" + userIdNumber + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
