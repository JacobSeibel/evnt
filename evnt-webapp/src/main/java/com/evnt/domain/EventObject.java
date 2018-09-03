package com.evnt.domain;

import com.vaadin.server.VaadinSession;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.time.*;
import java.time.temporal.TemporalUnit;
import java.util.Date;
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
    private boolean isActive;
}
