package com.evnt.domain;

import lombok.*;

import java.util.Date;

@Getter @Setter @ToString @NoArgsConstructor
public class QueuedEmail {
    private int pk;
    private Email email;
    private User recipient;
    private EventObject event;
    private User sender;
    private Date sendDate;

    public QueuedEmail(Email email, User recipient, EventObject event, User sender, Date sendDate){
        this.email = email;
        this.recipient = recipient;
        this.event = event;
        this.sender = sender;
        this.sendDate = sendDate;
    }
}
