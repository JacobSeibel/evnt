package evntapi.persistence;

import evntapi.domain.User;
import evntapi.persistence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper mapper;

    @Override
    public List<User> findAll() {
        List<User> test = mapper.findAll();
        return test;
    }
}