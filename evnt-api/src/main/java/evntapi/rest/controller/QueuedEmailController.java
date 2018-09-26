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
        return queuedEmailService.insert(queuedEmail);
    }

    @DeleteMapping("{pk}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("pk") int pk){
        queuedEmailService.delete(pk);
    }
}
