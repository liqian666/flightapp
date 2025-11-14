package epam.autotesting.flightbooking.controller;

import epam.autotesting.flightbooking.model.UserInfo;
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
    public ResponseEntity<String> register(@RequestBody UserInfo userInfo) {
        if (userInfo.getUserName() == null || userInfo.getPassword() == null || userInfo.getUserId() == null) {
            throw new IllegalArgumentException("User has null field(s): username, password, or userId");
        }
        else {
            userService.saveUser(userInfo);
            return ResponseEntity.ok("User registered successfully");
        }
    }

    @GetMapping
    public ResponseEntity<UserInfo> login(@RequestParam String username, @RequestParam String password) {
        List<UserInfo> usersInfo = userService.findAllUsers();
        UserInfo userInfo =  usersInfo.stream()
                .filter(user -> username.equals(user.getUserName()) && password.equals(user.getPassword()))
                .findFirst()
                .orElse(null);

        if(userInfo!=null){
            return ResponseEntity.ok(userInfo);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}")
    public UserInfo findUserById(@PathVariable String userId) {
        return userService.findUserById(userId);
    }

}
