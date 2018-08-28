package com.evnt.persistence;

import com.evnt.domain.SecurityRole;
import com.evnt.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class SecurityRoleDelegateService{
    private static final String URL = RestConstants.EVNT_API_BASE +"securityRole/";

    public List<SecurityRole> findAll(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<SecurityRole>> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<SecurityRole>>(){});
        return response.getBody();
    }

    public SecurityRole findByPk(int pk){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SecurityRole> response =
                restTemplate.exchange(
                        URL+pk,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<SecurityRole>(){});
        return response.getBody();
    }
}
