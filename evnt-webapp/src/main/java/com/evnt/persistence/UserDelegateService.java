package com.evnt.persistence;

import com.evnt.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserDelegateService {

    public User findByUsername(String username){
        return new User();
    }
}
