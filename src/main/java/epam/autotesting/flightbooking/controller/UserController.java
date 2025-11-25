package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.UserInfo;
import epam.autotesting.flightbooking.requestsresponses.ApiResponse;
import epam.autotesting.flightbooking.requestsresponses.ResponseHelper;
import epam.autotesting.flightbooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserInfo userInfo) {
        if (userInfo.getUserName() == null
                || userInfo.getPassword() == null
                || userInfo.getIdentityCardType() == null
                || userInfo.getIdentityCardNumber() == null) {
            throw new IllegalArgumentException("User has null field(s): username, password, or userId");
        }
        else {
            userService.saveUser(userInfo);
            return ResponseHelper.success(userInfo);
        }
    }

    @GetMapping("/login")
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
            return ResponseHelper.userNotFound("username: "+username+" password: "+password);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> findUserById(@RequestParam IDType identityCardType,
                                                    @RequestParam String identityCardNumber) {
       return userService.findUserByIdTypeAndIdNumber(identityCardType, identityCardNumber)
                .map(ResponseHelper::success)
                .orElseGet(() -> ResponseHelper.userNotFound("UserIdType: "+identityCardType +
                        " UserIdNumber: "+identityCardNumber));
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam IDType identityCardType,
                                                  @RequestParam String identityCardNumber) {
        Optional<UserInfo> userInfo = userService.findUserByIdTypeAndIdNumber(identityCardType, identityCardNumber);
        if (userInfo.isPresent()) {
            int deleted = userService.deleteUserByIdTypeAndIdNumber(identityCardType, identityCardNumber);
            if (deleted > 0) {
                return ResponseHelper.success(userInfo);
            }
            return ResponseHelper.failedToDelete("UserIdType: "+identityCardType +
                    " UserIdNumber: "+identityCardNumber);
        }

        return ResponseHelper.userNotFound("UserIdType: "+identityCardType +
                " UserIdNumber: "+identityCardNumber);
    }

}
