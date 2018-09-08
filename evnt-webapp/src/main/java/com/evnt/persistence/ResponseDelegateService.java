package com.evnt.persistence;

import com.evnt.domain.Response;
import com.evnt.domain.SecurityRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class ResponseDelegateService {
    private static final String URL = RestConstants.EVNT_API_BASE +"response/";

    public List<Response> findAll(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Response>> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Response>>(){});
        return response.getBody();
    }

    public Response findByPk(int pk){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response =
                restTemplate.exchange(
                        URL+pk,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<Response>(){});
        return response.getBody();
    }
}
