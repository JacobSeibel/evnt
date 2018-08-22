package com.evnt.ui.views;

import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.events.LogoutEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = LogoutView.NAME)
public class LogoutView extends Navigator.EmptyView {

    public static final String NAME = "logout";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        EvntWebappUI.getCurrent().getEventbus().post(new LogoutEvent(this));
    }
}
