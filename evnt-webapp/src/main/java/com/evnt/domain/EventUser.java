package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class EventUser {
    private int eventFk;
    private User user;
    private Role role;
    private Response response;
    private Date responseDate;

    public void changeResponse(Response response){
        this.response = response;
        this.responseDate = new Date();
    }

    public void changeRole(Role role){
        this.role = role;
    }
}
