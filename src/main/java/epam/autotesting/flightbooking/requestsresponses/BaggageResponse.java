package epam.autotesting.flightbooking.requestsresponses;

import epam.autotesting.flightbooking.helper.BaggageType;
import epam.autotesting.flightbooking.model.Baggage;

import java.util.List;

public class BaggageResponse {
    private List<BaggageType> baggageTypeList;
    private String identityCardNumber;

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public List<BaggageType> getBaggageTypeList() {
        return baggageTypeList;
    }

    public void setBaggageTypeList(List<BaggageType> baggageTypeList) {
        this.baggageTypeList = baggageTypeList;
    }
}
