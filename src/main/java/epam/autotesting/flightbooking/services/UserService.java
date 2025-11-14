package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.model.UserInfo;
import epam.autotesting.flightbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserInfo saveUser(UserInfo user) {
        return userRepository.save(user);
    }

    public UserInfo findUserById(String id) {
        return userRepository.findByUserId(id);
    }

    public List<UserInfo> findAllUsers() {
        return userRepository.findAll();
    }
}
