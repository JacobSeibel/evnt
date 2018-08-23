package com.evnt.ui.events;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.EventObject;

public class LogoutEvent extends EventObject {
    public LogoutEvent(Object source) {
        super(source);
        SecurityContextHolder.clearContext();
    }
}
