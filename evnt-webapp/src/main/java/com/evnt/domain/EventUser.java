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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventUser eu = (EventUser) obj;
        boolean isEqual = eventFk == eu.eventFk;
        isEqual &= user.equals(eu.user);
        isEqual &= role.getPk().equals(eu.getRole().getPk());
        if(response == null && eu.getResponse() == null){
            isEqual &= true;
        }else if(response != null && eu.getResponse() != null) {
            isEqual &= response.getPk().equals(eu.getResponse().getPk());
            isEqual &= responseDate.equals(eu.getResponseDate());
        }else{
            return false;
        }

        return isEqual;
    }
}
