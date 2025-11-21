package epam.autotesting.flightbooking.requestsresponses;

import epam.autotesting.flightbooking.helper.BaggageType;
import epam.autotesting.flightbooking.model.Baggage;

import java.util.List;

public class BaggageResponse {
    private List<BaggageType> baggageTypeList;
    private Long passengerId;

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public List<BaggageType> getBaggageTypeList() {
        return baggageTypeList;
    }

    public void setBaggageTypeList(List<BaggageType> baggageTypeList) {
        this.baggageTypeList = baggageTypeList;
    }
}
