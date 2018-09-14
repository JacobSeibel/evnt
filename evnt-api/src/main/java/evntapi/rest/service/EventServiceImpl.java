package evntapi.rest.service;

import evntapi.domain.*;
import evntapi.rest.mapper.EventMapper;
import evntapi.rest.mapper.EventUserMapper;
import evntapi.rest.mapper.PositionedImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService{
    @Autowired
    private EventMapper mapper;
    @Autowired
    private PositionedImageMapper positionedImageMapper;
    @Autowired
    private EventUserMapper eventUserMapper;

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
    public Event insert(Event event, int creatorFk) {
        positionedImageMapper.insert(event.getEventPhoto());
        mapper.insert(event);
        EventUser creator = new EventUser();
        creator.setEventFk(event.getPk());
        creator.setUser(new User(creatorFk));
        creator.setResponse(new Response(Response.GOING));
        creator.setRole(new Role(Role.CREATOR));
        creator.setResponseDate(new Date());
        eventUserMapper.insert(creator);
        return event;
    }

    @Override
    public Event update(Event event){
        if(event.getEventPhoto().getPk() == null) positionedImageMapper.insert(event.getEventPhoto());
        mapper.update(event);
        return event;
    }
}
