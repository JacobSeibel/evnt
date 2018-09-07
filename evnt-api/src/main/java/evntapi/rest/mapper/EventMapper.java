package evntapi.rest.mapper;

import evntapi.domain.Event;
import evntapi.domain.EventUser;
import evntapi.domain.SecurityRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EventMapper {
    List<Event> findAll();
    Event findByPk(int pk);
    List<EventUser> findEventUsersByPk(int pk);
    List<Event> findByUserFk(int userFk);
    void insert(Event event);
    void insertEventUser(EventUser eventUser);
    void deleteEventUser(@Param("eventFk") int eventFk, @Param("userFk") int userFk);
}
