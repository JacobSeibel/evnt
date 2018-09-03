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
    //TODO: Figure this out
    private File eventPhoto;
    private boolean isActive = true;

    private List<EventUser> eventUsers;

    @JsonIgnore
    public User getCreator(){
        for (EventUser eventUser : eventUsers){
            if(eventUser.getRoleFk() == Role.CREATOR){
                return eventUser.getUser();
            }
        }
        return null;
    }

    @JsonIgnore
    public List<User> getHosts(){
        List<User> hosts = new ArrayList<>();
        for (EventUser eventUser : eventUsers){
            if(eventUser.getRoleFk() == Role.CREATOR || eventUser.getRoleFk() == Role.HOST){
                hosts.add(eventUser.getUser());
            }
        }
        return hosts;
    }
}
