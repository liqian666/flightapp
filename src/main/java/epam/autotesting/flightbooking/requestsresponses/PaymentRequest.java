package epam.autotesting.flightbooking.requestsresponses;

import epam.autotesting.flightbooking.helper.PaymentMethod;

public class PaymentRequest {
    private Long bookingId;
    private Double amount;
    private PaymentMethod paymentMethod;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
