package evntapi.rest.controller;

import evntapi.domain.EventUser;
import evntapi.domain.QueuedEmail;
import evntapi.rest.RestConstants;
import evntapi.rest.service.QueuedEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.EVNT_API+"/queuedEmail")
public class QueuedEmailController {

    @Autowired
    private QueuedEmailService queuedEmailService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QueuedEmail> findAll(){
        return queuedEmailService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QueuedEmail insert(@RequestBody QueuedEmail queuedEmail){
        return queuedEmailService.find(queuedEmail.getEmail().getPk(), queuedEmail.getRecipient().getPk(), queuedEmail.getEvent().getPk()) == null ? queuedEmailService.insert(queuedEmail) : queuedEmail;
    }

    @DeleteMapping("/emailFk/{emailFk}/recipientFk/{recipientFk}/eventFk/{eventFk}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("emailFk") int emailFk, @PathVariable("recipientFk") int recipientFk, @PathVariable("eventFk") int eventFk){
        queuedEmailService.delete(emailFk, recipientFk, eventFk);
    }

    @DeleteMapping("/recipientFk/{recipientFk}/eventFk/{eventFk}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByRecipientAndEvent(@PathVariable("recipientFk") int recipientFk, @PathVariable("eventFk") int eventFk){
        queuedEmailService.deleteByRecipientAndEvent(recipientFk, eventFk);
    }
}
