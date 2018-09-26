package com.evnt.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class Email {
    public static final int INVITATION = 1;
    public static final int EVENT_IS_SOON = 2;
    public static final int RSVP_REMINDER = 3;
    public static final int RSVP_NOTIFICATION = 4;
    public static final int EVENT_UPDATED = 5;

    private Integer pk;
    private String name;
    private String description;
    private String subjectLine;
    private String freemarkerTemplate;
    private boolean isActive;

    public Email(int pk){
        this.pk = pk;
    }
}
