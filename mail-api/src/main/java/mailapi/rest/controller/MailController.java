package mailapi.rest.controller;

import mailapi.domain.MailParams;
import mailapi.rest.RestConstants;
import mailapi.rest.service.MailService;
import lombok.extern.slf4j.Slf4j;
import mailapi.util.MailUtils;
import mailapi.util.exception.EmailSendException;
import mailapi.util.exception.IllegalParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@Slf4j
@RequestMapping(RestConstants.MAIL_API)
public class MailController {

    private static final String DEFAULT_FROM = "evnt@donotreply.com";

    @Autowired
    private MailService mailService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postMailRequest(@RequestBody MailParams mailParams){
        MailUtils.validateParam("to", mailParams.getTo());
        if(!StringUtils.hasText(mailParams.getFrom())) mailParams.setFrom(DEFAULT_FROM);
        MailUtils.validateParam("subject", mailParams.getSubject());
        MailUtils.validateParam("body", mailParams.getBody());

        try {
            mailService.sendMail(mailParams);
        } catch (Exception e){
            throw new EmailSendException();
        }
    }
}
