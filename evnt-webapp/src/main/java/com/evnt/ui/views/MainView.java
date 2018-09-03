package com.evnt.ui.views;

import com.evnt.domain.EventObject;
import com.evnt.domain.SecurityRole;
import com.evnt.domain.User;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.events.LogoutEvent;
import com.evnt.ui.events.UserLoggedInEvent;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Secured(SecurityRole.ROLE_USER)
@SpringView(name = MainView.NAME)
public class MainView extends AbstractView {

    @Autowired
    private UserAuthenticationService userAuthService;
    @Autowired
    private EventDelegateService eventService;

    public final static String NAME = "main";

    @SuppressWarnings("unchecked")
    @PostConstruct
    void init() {
        if(userAuthService.loggedInUser() != null) {
            Label welcomeLabel = new Label("Welcome back, " + userAuthService.loggedInUser().getDisplayName() + "!");

            Grid<EventObject> eventsGrid = new Grid<>();
            List<EventObject> events = eventService.findByUserFk(userAuthService.loggedInUser().getPk());
            eventsGrid.setItems(events);
            Grid.Column nameColumn = eventsGrid.addColumn(EventObject::getName, new ButtonRenderer<>(click -> {
                EvntWebappUI.getUiService().postNavigationEvent(this, ViewEventView.NAME+"/?eventPk="+click.getItem().getPk());
            })).setCaption("Name");
            Grid.Column creatorColumn = eventsGrid.addColumn(event -> event.getCreator().getDisplayName()).setCaption("Created By");
            Grid.Column startDateColumn = eventsGrid.addColumn(EventObject::getStartDate).setCaption("Start Date");

            eventsGrid.setSortOrder(GridSortOrder.asc(startDateColumn).thenDesc(nameColumn).thenDesc(creatorColumn));

            addComponent(welcomeLabel);
            addComponent(eventsGrid);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}