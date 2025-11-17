package epam.autotesting.flightbooking.requestsresponses;

import epam.autotesting.flightbooking.helper.BookingStatus;
import epam.autotesting.flightbooking.helper.PaymentStatus;
import epam.autotesting.flightbooking.model.Passenger;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {
    private String userId;
    private String flightNumber;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    private List<String> passengersIdNumbers;
    private List<String> seats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getPassengersIdNumbers() {
        return passengersIdNumbers;
    }

    public void setPassengersIdNumbers(List<String> passengersIdNumbers) {
        this.passengersIdNumbers = passengersIdNumbers;
    }
}
