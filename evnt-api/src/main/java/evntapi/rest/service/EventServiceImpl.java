package evntapi.rest.service;

import evntapi.domain.*;
import evntapi.rest.mapper.EventMapper;
import evntapi.rest.mapper.SecurityRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService{
    @Autowired
    private EventMapper mapper;

    @Override
    public List<Event> findAll() {
        return mapper.findAll();
    }

    @Override
    public Event findByPk(int pk) {
        return mapper.findByPk(pk);
    }

    @Override
    public List<Event> findByUserFk(int userFk) {
        return mapper.findByUserFk(userFk);
    }

    @Override
    @Transactional
    public void insert(Event event, int creatorFk) {
        mapper.insert(event);
        EventUser creator = new EventUser();
        creator.setEventFk(event.getPk());
        creator.setUserFk(creatorFk);
        creator.setResponseFk(Response.GOING);
        creator.setRoleFk(Role.CREATOR);
        creator.setResponseDate(new Date());
        mapper.insertEventUser(creator);
    }
}
