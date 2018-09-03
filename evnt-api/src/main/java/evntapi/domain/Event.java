package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.Date;
import java.util.List;

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
    private String eventPhoto;
    private boolean isActive;

    private List<EventUser> eventUsers;
}
