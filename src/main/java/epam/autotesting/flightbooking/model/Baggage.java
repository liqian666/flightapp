package epam.autotesting.flightbooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import epam.autotesting.flightbooking.helper.BaggageType;
import jakarta.persistence.*;

@Entity
@Table(name = "baggage")
public class Baggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private BaggageType baggageType;
    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BaggageType getBaggageType() {
        return baggageType;
    }

    public void setBaggageType(BaggageType baggageType) {
        this.baggageType = baggageType;
    }

    @Override
    public String toString() {
        return "Baggage{" +
                "id=" + id +
                ", weight=" + baggageType +
                ", passengerId=" + (passenger != null ? passenger.getPassengerId() : null) +
                '}';
    }
}
