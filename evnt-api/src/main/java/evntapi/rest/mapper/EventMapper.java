package evntapi.rest.mapper;

import evntapi.domain.Event;
import evntapi.domain.EventUser;
import evntapi.domain.SecurityRole;

import java.util.List;

public interface EventMapper {
    List<Event> findAll();
    Event findByPk(int pk);
    Event findByUserFk(int userFk);
    void insert(Event event);
    void insertEventUser(EventUser eventUser);
}
