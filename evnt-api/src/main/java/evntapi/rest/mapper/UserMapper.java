package evntapi.rest.mapper;

import evntapi.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    List<User> findAll();
    User findByUsername(String username);
    User findByEmail(String email);
    void insert(User user);
    void insertUserSecurityRole(@Param("userFk") int userFk, @Param("securityRoleFk") int securityRoleFk);
}
