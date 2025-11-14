package epam.autotesting.flightbooking.requests;

import epam.autotesting.flightbooking.model.Passenger;

import java.util.List;

public class BookingRequest {
    private String userId;
    private String flightNumber;
    private List<Passenger> passengers;

    public BookingRequest() {}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "passengerId='" + userId + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
