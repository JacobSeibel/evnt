package com.evnt.ui.views;

import com.evnt.domain.*;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.EventUserDelegateService;
import com.evnt.persistence.ResponseDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.components.ManageInvitesOverlay;
import com.evnt.util.DateUtils;
import com.evnt.util.ParamUtils;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Secured(SecurityRole.ROLE_USER)
@SpringView(name = ViewEventView.NAME)
public class ViewEventView extends AbstractView {

    @Autowired
    private UserAuthenticationService userAuthService;
    @Autowired
    private EventDelegateService eventService;
    @Autowired
    private EventUserDelegateService eventUserService;
    @Autowired
    private UserDelegateService userService;
    @Autowired
    private ResponseDelegateService responseService;

    public final static String NAME = "view-event";

    private EventObject event = null;
    private Grid<EventUser> inviteesGrid = null;
    private Layout invitedLayout = null;
    private EventUser thisUserOnEvent = null;

    @SuppressWarnings("unchecked")
    private void build(){
        thisUserOnEvent = event.findUserOnEvent(userAuthService.loggedInUser().getPk());
        if(thisUserOnEvent == null){
            EvntWebappUI.getUiService().postNavigationEvent(this, AccessDeniedView.NAME);
            return;
        }

        if(thisUserOnEvent.getRoleFk() != Role.CREATOR) {
            HorizontalLayout askForResponseBanner = new HorizontalLayout();
            String selectResponseString = thisUserOnEvent.getResponse() != null ? "You are " : "Let "+event.getCreator().getDisplayName()+" know if you can make it!";
            Label selectResponseLabel = new Label(selectResponseString);
            HorizontalLayout buttonLayout = new HorizontalLayout();
            for (Response response : responseService.findAll()) {
                if (!(response.getPk() == Response.MAYBE) || event.isAllowMaybes()) {
                    Button responseBtn = new Button(response.getName());
                    if (response.equals(thisUserOnEvent.getResponse())) {
                        responseBtn.setIcon(VaadinIcons.CHECK);
                    }
                    responseBtn.addClickListener(click -> {
                        respond(thisUserOnEvent, response);
                        EvntWebappUI.getUiService().postNavigationEvent(this, ViewEventView.NAME + "/?eventPk=" + event.getPk());
                    });
                    buttonLayout.addComponent(responseBtn);
                }
            }
            askForResponseBanner.addComponents(selectResponseLabel, buttonLayout);
            addComponent(askForResponseBanner);
        }

        //TODO: Event Photo

        Label eventTitleLabel = new Label(event.getName());
        Label locationLabel = new Label(event.getLocation());
        locationLabel.setCaption("Location");
        Label startDateLabel = new Label(DateUtils.getPresentableDate(event.getStartDate()));
        startDateLabel.setCaption("Start Time");
        RichTextArea descriptionRichTextArea = new RichTextArea("Description");
        descriptionRichTextArea.setValue(event.getDescription());
        descriptionRichTextArea.setReadOnly(true);

        inviteesGrid = new Grid<>();
        inviteesGrid.setItems(event.getEventUsers());
        Grid.Column nameColumn = inviteesGrid.addColumn(user -> user.getUser().getDisplayName() + (user.getRoleFk() == Role.CREATOR || user.getRoleFk() == Role.HOST ? " ("+user.getRole().getName()+")" : "")).setCaption("Name");
        Grid.Column responseColumn = inviteesGrid.addColumn(user -> user.getResponse() != null ? user.getResponse().getName() : "");
        inviteesGrid.addColumn(EventUser::getResponseDate);

        inviteesGrid.setSortOrder(GridSortOrder.asc(responseColumn).thenDesc(nameColumn));

        addComponent(eventTitleLabel);
        addComponent(locationLabel);
        addComponent(startDateLabel);
        if(event.getEndDate() != null) {
            Label endDateLabel = new Label(DateUtils.getPresentableDate(event.getEndDate()));
            endDateLabel.setCaption("End Time");
            addComponent(endDateLabel);
        }
        addComponent(descriptionRichTextArea);
        invitedLayout = new HorizontalLayout(getInvitedComponent());
        addComponent(invitedLayout);

        addComponent(inviteesGrid);
    }

    private Component getInvitedComponent(){
        String invitedCountString = event.getAttendingCount() + " Attending, "+event.getNoResponseCount()+" Invited";
        if(thisUserOnEvent.getRoleFk() == Role.HOST || thisUserOnEvent.getRoleFk() == Role.CREATOR){
            Button manageInvites = new Button(invitedCountString);
            manageInvites.addClickListener(click -> launchManageInvitesOverlay());
            return manageInvites;
        }else{
            return new Label(invitedCountString);
        }
    }

    private void launchManageInvitesOverlay() {
        ManageInvitesOverlay manageInvitesOverlay = new ManageInvitesOverlay(event, userService, eventService, eventUserService);
        manageInvitesOverlay.addCloseListener(close -> {
            inviteesGrid.setItems(eventUserService.findByEventFk(event.getPk()));
            invitedLayout.removeAllComponents();
            invitedLayout.addComponent(getInvitedComponent());
        });
        EvntWebappUI.getCurrent().addWindow(manageInvitesOverlay);
    }

    private void respond(EventUser eventUser, Response response){
        eventUser.changeResponse(response);
        eventUserService.update(eventUser);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Integer eventPk = ParamUtils.getIntegerParam("eventPk");
        this.event = eventService.findByPk(eventPk);

        build();
    }
}