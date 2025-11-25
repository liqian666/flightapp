package epam.autotesting.flightbooking.requestsresponses;

import org.springframework.http.ResponseEntity;

public class ResponseHelper {
    // Success responses
    public static ResponseEntity<ApiResponse> success(String code, String message, Object data) {
        return ResponseEntity.ok(new ApiResponse(true, code, message, data));
    }

    public static ResponseEntity<ApiResponse> success(Object data) {
        return success(ResponseCodes.SUCCESS, "Operation completed successfully", data);
    }

    public static ResponseEntity<ApiResponse> badRequest(String code, String message, Object data) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, code, message, data));
    }

    public static ResponseEntity<ApiResponse> userNotFound(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.USER_NOT_FOUND,
                "User not found",
                data);
    }

    public static ResponseEntity<ApiResponse> seatNotFound(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.SEAT_NOT_FOUND,
                "Seat not found",
                data);
    }

    public static ResponseEntity<ApiResponse> passengerNotFound(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.PASSENGER_NOT_FOUND,
                "Passenger not found",
                data);
    }

    public static ResponseEntity<ApiResponse> baggageNotFound(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.BAGGAGE_NOT_FOUND,
                "Baggage not found",
                data);
    }

    public static ResponseEntity<ApiResponse> paymentNotFound(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.PAYMENT_NOT_FOUND,
                "Payment not found",
                data);
    }

    public static ResponseEntity<ApiResponse> failedToDelete(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.DELETING_FAILED,
                "Failed to delete",
                data);
    }

    public static ResponseEntity<ApiResponse> flightNotFound(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.FLIGHT_NOT_FOUND,
                "Flight not found",
                data);
    }

    public static ResponseEntity<ApiResponse> failedToPay(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.PAYMENT_FAILED,
                "Failed to Pay",
                data);
    }

    public static ResponseEntity<ApiResponse> bookingNotFound(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.BOOKING_NOT_FOUND,
                "Booking not found",
                data);
    }

    public static ResponseEntity<ApiResponse> notEnoughInformation(Object data) {
        return ResponseHelper.badRequest(ResponseCodes.NOT_ENOUGH_INFORMATION,
                "Some fields are empty",
                data);
    }
}
