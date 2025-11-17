package epam.autotesting.flightbooking.requestsresponses;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.Baggage;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.util.List;

public class BookingPassengerRequest {
    private String firstName;
    private String lastName;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private IDType idType;
    private String idNumber;

    private String seatNumber;
    private List<Double> weights;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public IDType getIdType() {
        return idType;
    }

    public void setIdType(IDType idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }
}
