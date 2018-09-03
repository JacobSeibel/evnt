package com.evnt.spring.security;

import com.evnt.domain.User;
import com.evnt.persistence.UserDelegateService;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserDelegateService userService;

    private User loggedInUser;

    public boolean loginUser(Authentication authenticationRequest) {
        try {
            final Authentication authentication = authenticationManager.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if(authentication.isAuthenticated()) {
                return true;
            }else{
                Notification.show("Authentication error", "Password is incorrect", Notification.Type.ERROR_MESSAGE);
                return false;
            }
        } catch (AuthenticationException aExc) {
            Notification.show("Authentication error", "Could not authenticate", Notification.Type.ERROR_MESSAGE);
            return false;
        }
    }

    public User loggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated()) {
            if(loggedInUser == null || !loggedInUser.getUsername().equals(auth.getPrincipal())) {
                loggedInUser = userService.findByUsername((String) auth.getPrincipal());
            }
            return loggedInUser;
        }
        return null;
    }
}
