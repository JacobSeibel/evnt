package com.evnt.persistence;

import com.evnt.domain.EventObject;
import com.evnt.domain.EventUser;
import com.evnt.spring.security.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class EventUserDelegateService {
    private static final String URL = RestConstants.EVNT_API_BASE+"eventUser/";

    /*
    ##### GET #####
     */
    public List<EventUser> findByEventFk(int eventFk){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<EventUser>> response =
                restTemplate.exchange(
                        URL +"eventFk/"+ eventFk,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<EventUser>>() {});
        return response.getBody();
    }

    /*
    ##### POST #####
     */
    public EventUser insert(EventUser eventUser){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<EventUser> request = new HttpEntity<>(eventUser, headers);
        ResponseEntity<EventUser> response =
                restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<EventUser>(){});
        return response.getBody();
    }

    /*
    ##### PUT #####
     */

    public void update(EventUser eventUser){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<EventUser> request = new HttpEntity<>(eventUser, headers);
        restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<EventUser>(){});
    }

    /*
    ##### DELETE #####
     */
    public void delete(int eventFk, int userFk){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                URL + eventFk + "/" + userFk,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<EventObject>(){});
    }
}
