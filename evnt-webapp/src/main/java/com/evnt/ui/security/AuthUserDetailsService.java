package com.evnt.ui.security;

import com.evnt.persistence.UserDelegateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthUserDetailsService implements UserDetailsService{
    @Autowired
    private UserDelegateService userLookupService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userLookupService.findByUsername(username);
    }
}
