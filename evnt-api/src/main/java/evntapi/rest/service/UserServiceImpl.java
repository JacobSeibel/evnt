package evntapi.rest.service;

import evntapi.domain.SecurityRole;
import evntapi.domain.User;
import evntapi.rest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public User findByEmail(String email) {
        return mapper.findByEmail(email);
    }

    @Override
    @Transactional
    public void insert(User user) {
        mapper.insert(user);
        for(SecurityRole role : user.getSecurityRoles()) {
            mapper.insertUserSecurityRole(user.getPk(), role.getPk());
        }
    }
}
