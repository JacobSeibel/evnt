package com.evnt.persistence;

import com.evnt.domain.Response;
import com.evnt.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class RoleDelegateService {
    private static final String URL = RestConstants.EVNT_API_BASE +"role/";

    public List<Role> findAll(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Role>> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Role>>(){});
        return response.getBody();
    }

    public Role findByPk(int pk){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Role> response =
                restTemplate.exchange(
                        URL+pk,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Role>(){});
        return response.getBody();
    }
}
