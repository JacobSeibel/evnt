package com.evnt.ui.views;

import com.evnt.domain.*;
import com.evnt.persistence.*;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.Theme;
import com.evnt.ui.components.ManageInvitesOverlay;
import com.evnt.ui.components.RepositionableImage;
import com.evnt.util.DateUtils;
import com.evnt.util.ParamUtils;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
    private RoleDelegateService roleService;
    @Autowired
    private UserDelegateService userService;
    @Autowired
    private ResponseDelegateService responseService;

    public final static String NAME = "view-event";

    private EventObject event = null;
    private Layout invitedLayout = null;
    private EventUser thisUserOnEvent = null;
    private boolean isHost = false;
    private RepositionableImage eventPhotoImage = null;

    @SuppressWarnings("unchecked")
    private void build(){
        thisUserOnEvent = event.findUserOnEvent(userAuthService.loggedInUser().getPk());
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

        Label eventTitleLabel = new Label(event.getName());

        eventPhotoImage = new RepositionableImage(true);
        eventPhotoImage.getImage().setSource(
                new StreamResource((StreamResource.StreamSource) () ->
                        new ByteArrayInputStream(event.getEventPhoto().getImage()), "streamedSourceFromByteArray"));

        Button testButton = new Button("TEST");
        testButton.addClickListener(click -> {
            eventPhotoImage.setScrollTop(event.getEventPhoto().getScrollTop());
        });

        Label locationLabel = new Label(event.getLocation());
        locationLabel.setCaption("Location");
        Label startDateLabel = new Label(DateUtils.getPresentableDate(event.getStartDate()));
        startDateLabel.setCaption("Start Time");
        RichTextArea descriptionRichTextArea = new RichTextArea("Description");
        descriptionRichTextArea.setValue(event.getDescription());
        descriptionRichTextArea.setReadOnly(true);

        addComponent(eventTitleLabel);
        addComponent(eventPhotoImage);
        addComponent(testButton);
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

        eventPhotoImage.setScrollLeft(event.getEventPhoto().getScrollLeft());
        eventPhotoImage.setScrollTop(event.getEventPhoto().getScrollTop());
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
        ManageInvitesOverlay manageInvitesOverlay = new ManageInvitesOverlay(event, isHost, userService, eventUserService, roleService);
        manageInvitesOverlay.addCloseListener(close -> {
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
    public void enter(ViewChangeListener.ViewChangeEvent changeEvent) {
        Integer eventPk = ParamUtils.getIntegerParam("eventPk");
        this.event = eventService.findByPk(eventPk);

        build();

        EvntWebappUI.getUiService();

        eventPhotoImage.setScrollLeft(this.event.getEventPhoto().getScrollLeft());
        eventPhotoImage.setScrollTop(this.event.getEventPhoto().getScrollTop());
    }
}