package evntapi.rest.service;

import evntapi.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> findAll();
    List<User> findActive();
    User findByUsername(String username);
    User findByEmail(String email);
    void insert(User user);
}
