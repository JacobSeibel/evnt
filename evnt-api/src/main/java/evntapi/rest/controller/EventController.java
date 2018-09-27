package evntapi.rest.controller;

import evntapi.domain.Event;
import evntapi.domain.EventUser;
import evntapi.domain.Response;
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
    public List<Event> findByUserFk(@PathVariable("userFk") int userFk){
        return eventService.findByUserFk(userFk);
    }

    @GetMapping("/future/userFk/{userFk}")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> findFutureByUserFk(@PathVariable("userFk") int userFk){
        return eventService.findFutureByUserFk(userFk);
    }

    @PostMapping("/{creatorFk}")
    @ResponseStatus(HttpStatus.CREATED)
    public Event insert(@PathVariable("creatorFk") int creatorFk, @RequestBody Event event){
        return eventService.insert(event, creatorFk);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Event update(@RequestBody Event event){
        return eventService.update(event);
    }
}
