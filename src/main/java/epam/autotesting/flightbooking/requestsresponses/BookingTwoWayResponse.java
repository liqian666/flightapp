package epam.autotesting.flightbooking.requestsresponses;

import java.time.LocalDate;

public class BookingTwoWayResponse extends BookingResponse {
    private String returnFlightNumber;
    private LocalDate returnData;

    public String getReturnFlightNumber() {
        return returnFlightNumber;
    }

    public void setReturnFlightNumber(String returnFlightNumber) {
        this.returnFlightNumber = returnFlightNumber;
    }

    public LocalDate getReturnData() {
        return returnData;
    }

    public void setReturnData(LocalDate returnData) {
        this.returnData = returnData;
    }
}
