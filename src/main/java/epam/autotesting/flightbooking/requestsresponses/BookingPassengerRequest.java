package epam.autotesting.flightbooking.requestsresponses;

import epam.autotesting.flightbooking.helper.BaggageType;
import epam.autotesting.flightbooking.helper.IDType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.util.List;

public class BookingPassengerRequest {
    private String firstName;
    private String lastName;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private IDType identityCardType;
    private String identityCardNumber;

    private String seatNumber;
    private List<BaggageType> baggageTypes;

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

    public IDType getIdentityCardType() {
        return identityCardType;
    }

    public void setIdentityCardType(IDType identityCardType) {
        this.identityCardType = identityCardType;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public List<BaggageType> getBaggageTypes() {
        return baggageTypes;
    }

    public void setBaggageTypes(List<BaggageType> baggageTypes) {
        this.baggageTypes = baggageTypes;
    }
}
