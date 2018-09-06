package com.evnt.ui.views;

import com.evnt.domain.*;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.components.ManageInvitesOverlay;
import com.evnt.util.DateUtils;
import com.evnt.util.ParamUtils;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.net.URI;

@Secured(SecurityRole.ROLE_USER)
@SpringView(name = ViewEventView.NAME)
public class ViewEventView extends AbstractView {

    @Autowired
    private UserAuthenticationService userAuthService;
    @Autowired
    private EventDelegateService eventService;
    @Autowired
    private UserDelegateService userService;

    public final static String NAME = "view-event";

    private EventObject event = null;

    @SuppressWarnings("unchecked")
    private void build(){
        //TODO: Event Photo

        Label eventTitleLabel = new Label(event.getName());
        Label locationLabel = new Label(event.getLocation());
        locationLabel.setCaption("Location");
        Label startDateLabel = new Label(DateUtils.getPresentableDate(event.getStartDate()));
        startDateLabel.setCaption("Start Time");
        Label endDateLabel = new Label(DateUtils.getPresentableDate(event.getEndDate()));
        endDateLabel.setCaption("End Time");
        RichTextArea descriptionRichTextArea = new RichTextArea("Description");
        descriptionRichTextArea.setValue(event.getDescription());
        descriptionRichTextArea.setReadOnly(true);

        Button manageInvites = new Button("Manage Invites");
        manageInvites.addClickListener(click -> launchManageInvitesOverlay());

        Grid<EventUser> inviteesGrid = new Grid<>();
        inviteesGrid.setItems(event.getEventUsers());
        Grid.Column nameColumn = inviteesGrid.addColumn(user -> user.getUser().getDisplayName() + (user.getRoleFk() == Role.CREATOR || user.getRoleFk() == Role.HOST ? " ("+user.getRole().getName()+")" : "")).setCaption("Name");
        Grid.Column responseColumn = inviteesGrid.addColumn(user -> user.getResponse() != null ? user.getResponse().getName() : "");
        inviteesGrid.addColumn(EventUser::getResponseDate);

        inviteesGrid.setSortOrder(GridSortOrder.asc(responseColumn).thenDesc(nameColumn));

        addComponent(eventTitleLabel);
        addComponent(locationLabel);
        addComponent(startDateLabel);
        addComponent(endDateLabel);
        addComponent(descriptionRichTextArea);
        addComponent(manageInvites);
        addComponent(inviteesGrid);
    }

    private void launchManageInvitesOverlay() {
        EvntWebappUI.getCurrent().addWindow(new ManageInvitesOverlay(event, userService, eventService));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Integer eventPk = ParamUtils.getIntegerParam("eventPk");
        this.event = eventService.findByPk(eventPk);

        build();
    }
}