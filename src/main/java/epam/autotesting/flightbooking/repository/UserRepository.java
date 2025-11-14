package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo,Integer> {
    UserInfo findByFirstName(String firstName);
    UserInfo findByUserId(String userId);
    UserInfo save(UserInfo userInfo);

}
