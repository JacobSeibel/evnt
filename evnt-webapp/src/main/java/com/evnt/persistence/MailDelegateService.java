package com.evnt.persistence;

import com.evnt.domain.MailParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MailDelegateService {
    private static final String URL = RestConstants.MAIL_API_BASE;

    public void send(MailParams params){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MailParams> request = new HttpEntity<>(params, headers);
        restTemplate.exchange(
                URL,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<MailParams>(){});
    }
}
