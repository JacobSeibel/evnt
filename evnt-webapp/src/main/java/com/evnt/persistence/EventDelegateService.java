package com.evnt.persistence;

import com.evnt.domain.EventObject;
import com.evnt.domain.EventUser;
import com.evnt.domain.Response;
import com.evnt.domain.User;
import com.evnt.spring.security.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EventDelegateService{
    private static final String URL = RestConstants.EVNT_API_BASE+"event/";

    @Autowired
    UserAuthenticationService userAuthenticationService;

    /*
    ##### GET #####
     */
    public List<EventObject> findAll(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<EventObject>> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<EventObject>>(){});
        return response.getBody();
    }

    public EventObject findByPk(int pk){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EventObject> response =
                restTemplate.exchange(
                        URL+pk,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<EventObject>(){});
        return response.getBody();
    }

    public List<EventObject> findByUserFk(int userFk){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<EventObject>> response =
                restTemplate.exchange(
                        URL+"userFk/"+userFk,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<EventObject>>(){});
        return response.getBody();
    }

    /*
    ##### POST #####
     */
    public void insert(EventObject event){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<EventObject> request = new HttpEntity<>(event, headers);
        restTemplate.exchange(
                URL+userAuthenticationService.loggedInUser().getPk(),
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<EventObject>(){});
    }
}
