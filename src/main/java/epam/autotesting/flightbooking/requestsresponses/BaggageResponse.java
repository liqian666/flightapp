package epam.autotesting.flightbooking.requestsresponses;

import java.util.List;

public class BaggageResponse {
    private List<Double> weight;
    private Long passengerId;

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public List<Double> getWeight() {
        return weight;
    }

    public void setWeight(List<Double> weight) {
        this.weight = weight;
    }
}
