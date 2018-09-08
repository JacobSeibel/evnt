package com.evnt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public void changeResponse(Response response){
        this.response = response;
        this.responseFk = response.getPk();
        this.responseDate = new Date();
    }
}
