package evntapi.rest.service;

import evntapi.domain.Event;
import evntapi.domain.EventUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventUserService {
    List<EventUser> findByEventFk(int eventFk);
    EventUser insert(EventUser eventUser);
    void update(EventUser eventUser);
    void delete(int eventFk, int userFk);
}