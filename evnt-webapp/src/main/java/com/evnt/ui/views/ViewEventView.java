package com.evnt.ui.views;

import com.evnt.domain.*;
import com.evnt.persistence.*;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.components.ManageInvitesOverlay;
import com.evnt.util.DateUtils;
import com.evnt.util.ParamUtils;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

@Secured(SecurityRole.ROLE_USER)
@SpringView(name = ViewEventView.NAME)
public class ViewEventView extends AbstractView {

    @Autowired
    private EventDelegateService eventService;
    @Autowired
    private EventUserDelegateService eventUserService;
    @Autowired
    private RoleDelegateService roleService;
    @Autowired
    private QueuedEmailDelegateService queuedEmailService;
    @Autowired
    private UserDelegateService userService;
    @Autowired
    private ResponseDelegateService responseService;
    @Autowired
    private UserAuthenticationService userAuthenticationService;

    public final static String NAME = "view-event";

    private EventObject event = null;
    private Layout invitedLayout = null;
    private EventUser thisUserOnEvent = null;
    private boolean isHost = false;

    @SuppressWarnings("unchecked")
    private void build(){
        thisUserOnEvent = event.findUserOnEvent(userAuthenticationService.loggedInUser().getPk());
        if(thisUserOnEvent == null){
            EvntWebappUI.getUiService().postNavigationEvent(this, AccessDeniedView.NAME);
            return;
        }

        if(!Role.isCreator(thisUserOnEvent.getRole())) {
            HorizontalLayout askForResponseBanner = new HorizontalLayout();
            String selectResponseString = thisUserOnEvent.getResponse() != null ? "You responded:" : "Let "+event.getCreator().getDisplayName()+" know if you can make it!";
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
        if(Role.isCreator(thisUserOnEvent.getRole()) || Role.isHost(thisUserOnEvent.getRole())){
            isHost = true;
            Button editBtn = new Button("Edit");
            editBtn.addClickListener(click -> EvntWebappUI.getUiService().postNavigationEvent(this, CreateEventView.NAME+"/?eventPk=" + event.getPk()));
            addComponent(editBtn);
        }

        Label eventTitleLabel = new Label("<h1>"+event.getName()+"</h1>", ContentMode.HTML);

        Image eventPhotoImage = new Image();
        if(event.getEventPhoto() != null) {
            eventPhotoImage.setSource(
                    new StreamResource((StreamResource.StreamSource) () ->
                            new ByteArrayInputStream(event.getEventPhoto()), event.getName()));
        }
        eventPhotoImage.setWidth("100%");

        Label locationLabel = new Label(event.getLocation());
        locationLabel.setCaption("Location");
        Label startDateLabel = new Label(DateUtils.getPresentableDate(event.getStartDate()));
        startDateLabel.setCaption("Start Time");
        RichTextArea descriptionRichTextArea = new RichTextArea("Description");
        descriptionRichTextArea.setValue(event.getDescription());
        descriptionRichTextArea.setReadOnly(true);
        descriptionRichTextArea.setSizeFull();

        addComponent(eventTitleLabel);
        addComponent(eventPhotoImage);
        addComponent(locationLabel);
        addComponent(startDateLabel);
        if(event.getEndDate() != null) {
            Label endDateLabel = new Label(DateUtils.getPresentableDate(event.getEndDate()));
            endDateLabel.setCaption("End Time");
            addComponent(endDateLabel);
        }
        invitedLayout = new HorizontalLayout(getInvitedComponent());
        addComponent(invitedLayout);
        addComponent(descriptionRichTextArea);
    }

    private Component getInvitedComponent(){
        String invitedCountString = event.getAttendingCount() + " Attending, "+event.getNoResponseCount()+" Invited";
        if(Role.isHost(thisUserOnEvent.getRole()) || Role.isCreator(thisUserOnEvent.getRole())){
            Button manageInvites = new Button(invitedCountString);
            manageInvites.addClickListener(click -> launchManageInvitesOverlay());
            return manageInvites;
        }else{
            return new Label(invitedCountString);
        }
    }

    private void launchManageInvitesOverlay() {
        ManageInvitesOverlay manageInvitesOverlay = new ManageInvitesOverlay(event, isHost, userService, eventUserService, roleService, queuedEmailService, userAuthenticationService);
        manageInvitesOverlay.addCloseListener(close -> {
            invitedLayout.removeAllComponents();
            invitedLayout.addComponent(getInvitedComponent());
        });
        EvntWebappUI.getCurrent().addWindow(manageInvitesOverlay);
    }

    private void respond(EventUser eventUser, Response response){
        eventUser.changeResponse(response);
        eventUserService.update(eventUser);

        //Send response 10 minutes from now
        Calendar sendDateCalendar = Calendar.getInstance();
        sendDateCalendar.add(Calendar.MINUTE, 10);
        Email email = new Email(Email.RSVP_REMINDER);

        for(User host : event.getHosts()){
            QueuedEmail qe = new QueuedEmail(
                    email,
                    host,
                    event,
                    userAuthenticationService.loggedInUser(),
                    sendDateCalendar.getTime());

            queuedEmailService.insert(qe);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent changeEvent) {
        Integer eventPk = ParamUtils.getIntegerParam("eventPk");
        if(eventPk != null) {
            this.event = eventService.findByPk(eventPk);
        }

        build();
    }
}