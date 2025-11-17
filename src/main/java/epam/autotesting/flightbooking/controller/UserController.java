package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.UserInfo;
import epam.autotesting.flightbooking.requestsresponses.ApiResponse;
import epam.autotesting.flightbooking.requestsresponses.ResponseCodes;
import epam.autotesting.flightbooking.requestsresponses.ResponseHelper;
import epam.autotesting.flightbooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserInfo userInfo) {
        if (userInfo.getUserName() == null || userInfo.getPassword() == null || userInfo.getUserId() == null) {
            throw new IllegalArgumentException("User has null field(s): username, password, or userId");
        }
        else {
            userService.saveUser(userInfo);
            return ResponseHelper.success(userInfo);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> login(@RequestParam String username, @RequestParam String password) {
        List<UserInfo> usersInfo = userService.findAllUsers();
        UserInfo userInfo =  usersInfo.stream()
                .filter(user -> username.equals(user.getUserName()) && password.equals(user.getPassword()))
                .findFirst()
                .orElse(null);

        if(userInfo!=null){
            return ResponseHelper.success(userInfo);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> findUserById(@PathVariable String userId) {
        if (userId == null) {
            return ResponseHelper.badRequest(ResponseCodes.USER_ID_EMPTY, "Payment Id is empty", null);
        }

        return userService.findUserById(userId)
                .map(ResponseHelper::success)
                .orElseGet(() -> ResponseHelper.badRequest(ResponseCodes.USER_NOT_FOUND, "Payment not found", userId));
    }

}
