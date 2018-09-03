package com.evnt.ui.views;

import com.evnt.ui.EvntWebappUI;
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
        layout.setMargin(true);
        layout.setSpacing(true);

        buildNavbar();

        setContent(layout);

        registerWithEventbus();
    }

    private void buildNavbar(){
        Panel navbarPanel = new Panel();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        final Link profileLink = new Link("Your Profile", new ExternalResource("#!" + ProfileView.NAME));
        profileLink.setIcon(VaadinIcons.USER);
        horizontalLayout.addComponent(profileLink);
        final Link invalidLink = new Link("Create Event", new ExternalResource("#!" + CreateEventView.NAME));
        invalidLink.setIcon(VaadinIcons.CALENDAR);
        horizontalLayout.addComponent(invalidLink);

        logoutLink = new LogoutLink();
        logoutLink.updateVisibility();
        horizontalLayout.addComponent(logoutLink);

        Link adminLink = new Link("Admin page", new ExternalResource("#!" + AdminView.NAME));
        adminLink.setIcon(VaadinIcons.LOCK);
        horizontalLayout.addComponent(adminLink);

        Link aboutLink = new Link("About", new ExternalResource("#!" + AboutView.NAME));
        aboutLink.setIcon(VaadinIcons.QUESTION_CIRCLE);
        horizontalLayout.addComponent(aboutLink);
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
