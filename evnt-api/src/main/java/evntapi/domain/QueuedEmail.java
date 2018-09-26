package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class QueuedEmail {
    private int pk;
    private Email email;
    private User recipient;
    private Event event;
    private User sender;
    private Date sendDate;
}
