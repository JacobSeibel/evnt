package evntapi.persistence;

import evntapi.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> findAll();
    User findByUsername(String username);
}
