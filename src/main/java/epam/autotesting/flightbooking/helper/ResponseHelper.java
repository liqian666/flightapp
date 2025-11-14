package epam.autotesting.flightbooking.helper;

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
}
