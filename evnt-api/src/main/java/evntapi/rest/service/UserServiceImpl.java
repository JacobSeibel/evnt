package evntapi.rest.service;

import evntapi.domain.User;
import evntapi.rest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper mapper;

    @Override
    public List<User> findAll() {
        return mapper.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return mapper.findByUsername(username);
    }
}
