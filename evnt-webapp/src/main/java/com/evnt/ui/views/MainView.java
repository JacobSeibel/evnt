package com.evnt.ui.views;

import com.evnt.domain.EventObject;
import com.evnt.domain.SecurityRole;
import com.evnt.persistence.EventDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.util.List;

@Secured(SecurityRole.ROLE_USER)
@SpringView(name = MainView.NAME)
public class MainView extends AbstractView {

    @Autowired
    private EventDelegateService eventService;
    @Autowired
    private UserAuthenticationService userAuthenticationService;

    public final static String NAME = "main";

    @SuppressWarnings("unchecked")
    @PostConstruct
    void init() {
        if(userAuthenticationService.loggedInUser() != null) {
            Label welcomeLabel = new Label("Welcome back, " + userAuthenticationService.loggedInUser().getDisplayName() + "!");

            Grid<EventObject> eventsGrid = new Grid<>();
            List<EventObject> events = eventService.findFutureByUserFk(userAuthenticationService.loggedInUser().getPk());
            eventsGrid.setItems(events);
            Grid.Column nameColumn = eventsGrid.addColumn(EventObject::getName, new ButtonRenderer<>(click ->
                    EvntWebappUI.getUiService().postNavigationEvent(this, ViewEventView.NAME+"/?eventPk="+click.getItem().getPk()))).setCaption("Name");
            Grid.Column creatorColumn = eventsGrid.addColumn(event -> event.getCreator().getDisplayName()).setCaption("Created By");
            Grid.Column startDateColumn = eventsGrid.addColumn(EventObject::getStartDate).setCaption("Start Date");

            eventsGrid.setSortOrder(GridSortOrder.asc(startDateColumn).thenDesc(nameColumn).thenDesc(creatorColumn));

            addComponent(welcomeLabel);
            eventsGrid.setWidth("100%");
            eventsGrid.setHeight("100%");
            addComponent(eventsGrid);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}