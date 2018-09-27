package com.evnt.scheduled;

import com.evnt.domain.QueuedEmail;
import com.evnt.persistence.MailDelegateService;
import com.evnt.persistence.QueuedEmailDelegateService;
import com.evnt.util.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component @Slf4j
public class ScheduledTasks {
    @Autowired
    private QueuedEmailDelegateService queuedEmailService;
    @Autowired
    private MailDelegateService mailService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 600000)
    public void checkEmailQueue(){
        log.debug("Checking email queue...");
        List<QueuedEmail> queue = queuedEmailService.findAll();
        log.debug("Found {} emails waiting to be sent.", queue.size());
        List<QueuedEmail> toSend = new ArrayList<>();
        for(QueuedEmail queuedEmail : queue){
            if(queuedEmail.getSendDate().before(new Date())){
                toSend.add(queuedEmail);
            }
        }
        log.debug("Found {} emails ready for sending.", toSend.size());
        for(QueuedEmail sendEmail : toSend){
            mailService.send(MailTemplate.INSTANCE.getEmail(sendEmail));
            queuedEmailService.delete(sendEmail.getEmail().getPk(), sendEmail.getRecipient().getPk(), sendEmail.getEvent().getPk());
        }
    }
}
