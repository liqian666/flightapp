package epam.autotesting.flightbooking.repository;

import epam.autotesting.flightbooking.helper.IDType;
import epam.autotesting.flightbooking.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo,Integer> {
    public UserInfo findByFirstName(String firstName);
    public UserInfo save(UserInfo userInfo);
    public Optional<UserInfo> findByIdentityCardNumber(String userIdNumber);
    public Optional<UserInfo> findByUserId(Integer userId);
    public Optional<UserInfo> findByIdentityCardTypeAndIdentityCardNumber(IDType idType, String idNumber);

}
