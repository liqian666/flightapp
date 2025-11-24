package epam.autotesting.flightbooking.requestsresponses;

import java.time.LocalDate;

public class BookingTwoWayRequest extends BookingOneWayRequest {
    private LocalDate returnDate;
    private String returnFlightNumber;

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnFlightNumber() {
        return returnFlightNumber;
    }

    public void setReturnFlightNumber(String returnFlightNumber) {
        this.returnFlightNumber = returnFlightNumber;
    }
}
