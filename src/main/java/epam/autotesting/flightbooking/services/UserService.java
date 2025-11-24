package epam.autotesting.flightbooking.services;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.UserInfo;
import epam.autotesting.flightbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserInfo saveUser(UserInfo user) {
        return userRepository.save(user);
    }

    public Optional<UserInfo> findUserByIdTypeAndIdNumber(IDType idType, String idNumber) {
        return userRepository.findByIdTypeAndIdNumber(idType,idNumber);
    }

    public List<UserInfo> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserInfo> findUserByUserIdNumber(String userIdNumber) {
        return userRepository.findByIdNumber(userIdNumber);
    }
}
