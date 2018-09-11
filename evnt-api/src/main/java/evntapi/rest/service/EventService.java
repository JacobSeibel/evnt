package evntapi.rest.service;

import evntapi.domain.Event;
import evntapi.domain.EventUser;
import evntapi.domain.Response;
import evntapi.domain.SecurityRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    List<Event> findAll();
    Event findByPk(int pk);
    List<Event> findByUserFk(int userFk);
    Event insert(Event event, int creatorFk);
    Event update(Event event);
}