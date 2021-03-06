package evntapi.rest.service;

import evntapi.domain.EventUser;
import evntapi.domain.Role;
import evntapi.domain.User;
import evntapi.rest.mapper.EventUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventUserServiceImpl implements EventUserService{
    @Autowired
    private EventUserMapper mapper;

    @Override
    public List<EventUser> findByEventFk(int pk) {
        return mapper.findByEventFk(pk);
    }

    @Override
    public EventUser insert(EventUser eventUser) {
        mapper.insert(eventUser);
        return eventUser;
    }

    @Override
    public void update(EventUser eventUser){
        mapper.update(eventUser);
    }

    @Override
    public void delete(int eventFk, int userFk) {
        mapper.delete(eventFk, userFk);
    }
}
