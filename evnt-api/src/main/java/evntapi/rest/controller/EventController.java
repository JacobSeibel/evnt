package evntapi.rest.controller;

import evntapi.domain.Event;
import evntapi.domain.SecurityRole;
import evntapi.rest.RestConstants;
import evntapi.rest.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.EVNT_API+"/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Event> findAll(){
        return eventService.findAll();
    }

    @GetMapping("/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public Event findByPk(@PathVariable("pk") int pk){
        return eventService.findByPk(pk);
    }

    @GetMapping("/userFk/{userFk}")
    @ResponseStatus(HttpStatus.OK)
    public Event findByUserFk(@PathVariable("userFk") int userFk){
        return eventService.findByUserFk(userFk);
    }

    @PostMapping("/{creatorFk}")
    @ResponseStatus(HttpStatus.CREATED)
    public void insert(@PathVariable("creatorFk") int creatorFk, @RequestBody Event event){
        eventService.insert(event, creatorFk);
    }
}
