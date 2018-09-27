package com.evnt.ui.views;

import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.Theme;
import com.evnt.ui.components.LogoutLink;
import com.evnt.ui.events.LogoutEvent;
import com.evnt.ui.events.UserLoggedInEvent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

@Slf4j
public abstract class AbstractView extends Panel implements View {
    private VerticalLayout layout;
    private LogoutLink logoutLink;

    AbstractView() {
        setSizeFull();
        layout = new VerticalLayout();

        buildNavbar();

        setContent(layout);


        registerWithEventbus();
    }

    private void buildNavbar(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        final Link homeLink = new Link("evnt", new ExternalResource("#!" + MainView.NAME));
        horizontalLayout.addComponent(homeLink);
        final Link profileLink = new Link("Profile", new ExternalResource("#!" + ProfileView.NAME));
        profileLink.setIcon(VaadinIcons.USER);
        horizontalLayout.addComponent(profileLink);
        final Link invalidLink = new Link("Create Event", new ExternalResource("#!" + CreateEventView.NAME));
        invalidLink.setIcon(VaadinIcons.CALENDAR);
        horizontalLayout.addComponent(invalidLink);

        logoutLink = new LogoutLink();
        logoutLink.updateVisibility();
        horizontalLayout.addComponent(logoutLink);

        horizontalLayout.addStyleName(Theme.NAVBAR);

        addComponent(horizontalLayout);
    }

    public void addComponent(Component c) {
        layout.addComponent(c);
    }

    protected void registerWithEventbus() {
        EvntWebappUI.getCurrent().getEventbus().register(this);
    }

    @PreDestroy
    public void destroy() {
        log.debug("About to destroy {}", getClass().getName());
        EvntWebappUI.getCurrent().getEventbus().unregister(this);
    }


    @Subscribe
    public void userLoggedIn(UserLoggedInEvent event) {
        logoutLink.updateVisibility();
    }

    @Subscribe
    public void userLoggedOut(LogoutEvent event) {
        logoutLink.updateVisibility();
    }
}
