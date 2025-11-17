package epam.autotesting.flightbooking.requestsresponses;

public class ResponseCodes {

    // Success codes
    public static final String SUCCESS = "SUCCESS";
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";

    // Not Found errors (HTTP 404)
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_ID_EMPTY = "USER_ID_EMPTY";
    public static final String BOOKING_ID_EMPTY = "BOOKING_ID_EMPTY";
    public static final String PAYMENT_ID_EMPTY = "PAYMENT_ID_EMPTY";
    public static final String BOOKING_NOT_FOUND = "BOOKING_NOT_FOUND";

    public static final String NOT_ENOUGH_INFORMATION = "NOT_ENOUGH_INFORMATION";

    public static final String PAYMENT_NOT_FOUND = "PAYMENT_NOT_FOUND";

    public static final String BAGGAGE_NOT_FOUND = "BAGGAGE_NOT_FOUND";

    public static final String BOOKING_FAILED = "BOOKING_FAILED";

    // Bad Request errors (HTTP 400)
    public static final String FLIGHT_NUMBER_OR_PASSENGER_ID_ID_IS_NULL = "FLIGHT_NUMBER_OR_PASSENGER_ID_ID_IS_NULL";
    public static final String PASSENGER_NOT_FOUND = "PASSENGER_NOT_FOUND";
}
