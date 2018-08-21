package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.Date;

@Getter @Setter @ToString
public class Event {
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
    private boolean isActive;
}
