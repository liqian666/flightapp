package epam.autotesting.flightbooking.model;

import epam.autotesting.flightbooking.helper.PaymentMethod;
import epam.autotesting.flightbooking.helper.PaymentStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String passengerId;
    private String seatNumber;
    private int flightId;
    private int bookingId;

}
