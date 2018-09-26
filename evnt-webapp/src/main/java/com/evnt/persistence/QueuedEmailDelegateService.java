package com.evnt.persistence;

import com.evnt.domain.QueuedEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class QueuedEmailDelegateService {
    private static final String URL = RestConstants.EVNT_API_BASE+"queuedEmail/";

    /*
    ##### GET #####
     */
    public List<QueuedEmail> findAll(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<QueuedEmail>> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<QueuedEmail>>() {});
        return response.getBody();
    }

    /*
    ##### POST #####
     */
    public QueuedEmail insert(QueuedEmail queuedEmail){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<QueuedEmail> request = new HttpEntity<>(queuedEmail, headers);
        ResponseEntity<QueuedEmail> response =
                restTemplate.exchange(
                        URL,
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<QueuedEmail>(){});
        return response.getBody();
    }

    /*
    ##### DELETE #####
     */
    public void delete(int pk){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
                URL + pk,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<QueuedEmail>(){});
    }
}
