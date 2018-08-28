package com.evnt.persistence;

import com.evnt.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserDelegateService implements UserDetailsService{
    private static final String URL = RestConstants.EVNT_API_BASE+"user/";

    public List<User> findAll(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<User>> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<User>>(){});
        return response.getBody();
    }

    public User findByUsername(String username){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response =
                restTemplate.exchange(
                        URL+"username/"+username,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<User>(){});
        return response.getBody();
    }

    public User findByEmail(String email){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response =
                restTemplate.exchange(
                        URL+"email/"+email,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<User>(){});
        return response.getBody();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);

        User user = findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " is unknown");
        }

        log.debug("Loaded user {}", user);

        return findByUsername(username);
    }

    public User insert(User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<User> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<User>(){});
        return response.getBody();
    }
}
