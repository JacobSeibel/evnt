package com.evnt.ui.events;

import com.evnt.domain.User;
import com.google.common.base.Preconditions;

import java.util.EventObject;

public class UserLoggedInEvent extends EventObject {

    private User user;

    public UserLoggedInEvent(Object source, User user) {
        super(source);
        Preconditions.checkNotNull(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
