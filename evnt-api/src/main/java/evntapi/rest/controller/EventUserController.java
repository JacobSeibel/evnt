package evntapi.rest.controller;

import evntapi.domain.EventUser;
import evntapi.rest.RestConstants;
import evntapi.rest.service.EventUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.EVNT_API+"/eventUser")
public class EventUserController {

    @Autowired
    private EventUserService eventUserService;

    @GetMapping("/eventFk/{eventFk}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventUser> findByEventFk(@PathVariable("eventFk") int eventFk){
        return eventUserService.findByEventFk(eventFk);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventUser invite(@RequestBody EventUser eventUser){
        return eventUserService.insert(eventUser);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody EventUser eventUser){
        eventUserService.update(eventUser);
    }

    @DeleteMapping("/{eventFk}/{userFk}")
    @ResponseStatus(HttpStatus.OK)
    public void uninvite(@PathVariable("eventFk") int eventFk, @PathVariable("userFk") int userFk){
        eventUserService.delete(eventFk, userFk);
    }
}
