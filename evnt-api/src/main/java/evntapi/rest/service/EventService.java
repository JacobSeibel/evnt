package evntapi.rest.service;

import evntapi.domain.Event;
import evntapi.domain.SecurityRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    List<Event> findAll();
    Event findByPk(int pk);
    Event findByUserFk(int userFk);
    void insert(Event event, int creatorFk);
}
