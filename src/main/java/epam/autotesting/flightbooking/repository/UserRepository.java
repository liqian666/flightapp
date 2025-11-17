package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,Integer> {
    public UserInfo findByFirstName(String firstName);
    public Optional<UserInfo> findByUserId(String userId);
    public UserInfo save(UserInfo userInfo);

}
