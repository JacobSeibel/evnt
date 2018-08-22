package com.evnt.persistence;

import com.evnt.ui.events.NavigationEvent;
import com.vaadin.ui.UI;
import com.evnt.ui.EvntWebappUI;

public class VaadinUIServiceImpl implements VaadinUIService {

    @Override
    public void postNavigationEvent(Object source, String target) {
        EvntWebappUI.getCurrent().getEventbus().post(new NavigationEvent(source, target));
    }

    @Override
    public boolean isUserAnonymous() {
        return EvntWebappUI.getCurrent().isUserAnonymous();
    }
}
