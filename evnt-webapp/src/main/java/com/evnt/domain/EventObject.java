package com.evnt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.server.VaadinSession;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.time.*;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Getter @Setter @ToString
public class EventObject {
    private Integer pk;
    private String name;
    private String location;
    private Date startDate;
    private Date endDate;
    private String description;
    private boolean allowMaybes;
    private Date rsvpDate;
    private byte[] eventPhoto;
    private boolean isActive = true;

    private List<EventUser> eventUsers;

    @JsonIgnore
    public User getCreator(){
        for (EventUser eventUser : eventUsers){
            if(Role.isCreator(eventUser.getRole())){
                return eventUser.getUser();
            }
        }
        return null;
    }

    @JsonIgnore
    public List<User> getHosts(){
        List<User> hosts = new ArrayList<>();
        for (EventUser eventUser : eventUsers){
            if(Role.isCreator(eventUser.getRole()) || Role.isHost(eventUser.getRole())){
                hosts.add(eventUser.getUser());
            }
        }
        return hosts;
    }

    public EventUser findUserOnEvent(int userPk){
        for(EventUser eventUser : eventUsers){
            if(eventUser.getUser().getPk() == userPk) return eventUser;
        }
        return null;
    }

    @JsonIgnore
    public int getAttendingCount(){
        int count = 0;
        for(EventUser eventUser : eventUsers){
            if(eventUser.getResponse() != null && eventUser.getResponse().getPk() == Response.GOING) count++;
        }
        return count;
    }

    @JsonIgnore
    public int getNoResponseCount(){
        int count = 0;
        for(EventUser eventUser : eventUsers){
            if(eventUser.getResponse() == null) count++;
        }
        return count;
    }
}
