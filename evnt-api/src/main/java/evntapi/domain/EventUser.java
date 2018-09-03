package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class EventUser {
    private int eventFk;
    private int userFk;
    private int roleFk;
    private int responseFk;

    private User user;
    private Role role;
    private Response response;
    private Date responseDate;
}
