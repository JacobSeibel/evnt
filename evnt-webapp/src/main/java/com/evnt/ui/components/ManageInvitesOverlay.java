package com.evnt.ui.components;

import com.evnt.domain.EventObject;
import com.evnt.domain.EventUser;
import com.evnt.domain.Role;
import com.evnt.domain.User;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.EventUserDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.views.ViewEventView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;


public class ManageInvitesOverlay extends Window {

    private final UserDelegateService userService;
    private final EventDelegateService eventService;
    private final EventUserDelegateService eventUserService;

    private final EventObject event;
    private Grid<User> usersToInviteGrid;

    public ManageInvitesOverlay(EventObject event, UserDelegateService userService, EventDelegateService eventService, EventUserDelegateService eventUserService){
        this.event = event;
        this.userService = userService;
        this.eventService = eventService;
        this.eventUserService = eventUserService;
        build();
        center();
        setClosable(true);
        setModal(true);
        setHeight("60%");
        setWidth("40%");
    }

    private void build(){
        VerticalLayout content = new VerticalLayout();
        Label headerLabel = new Label("<h1>Manage Invites</h1>", ContentMode.HTML);
        content.addComponent(headerLabel);
        usersToInviteGrid = getGrid();
        content.addComponent(usersToInviteGrid);
        setContent(content);
    }

    private Grid<User> getGrid(){
        Grid<User> usersToInviteGrid = new Grid<>();
        usersToInviteGrid.setItems(userService.findActive());

        //TODO: Make sure invited users sort first, then uninvited users last
        usersToInviteGrid.addColumn(User::getDisplayName).setCaption("Name");
        usersToInviteGrid.addColumn(User::getUsername).setCaption("Username");
        usersToInviteGrid.addColumn(User::getEmail).setCaption("Email");
        usersToInviteGrid.addComponentColumn(this::getResponseLayout);

        usersToInviteGrid.setSizeFull();
        return usersToInviteGrid;
    }

    private HorizontalLayout getResponseLayout(User user){
        EventUser eventUser = event.findUserOnEvent(user.getPk());
        if(eventUser != null) {
            HorizontalLayout invitedLayout = new HorizontalLayout();
            String invitedString = eventUser.getResponse() != null ? eventUser.getResponse().getName() : "Invited";
            if(eventUser.getRoleFk() != Role.GUEST) invitedString += " ("+eventUser.getRole().getName()+")";
            Label invitedLabel = new Label(invitedString);
            invitedLabel.setWidth("10em");
            invitedLayout.addComponent(invitedLabel);
            if(eventUser.getRoleFk() != Role.CREATOR) {
                Button uninviteBtn = new Button();
                uninviteBtn.setIcon(VaadinIcons.CLOSE);
                uninviteBtn.addClickListener(click -> uninvite(user, uninviteBtn));
                invitedLayout.addComponent(uninviteBtn);
            }

            return invitedLayout;
        }else {
            HorizontalLayout inviteButtonLayout = new HorizontalLayout();
            Button inviteBtn = new Button("Invite");
            inviteBtn.addClickListener(click -> invite(user));
            inviteButtonLayout.addComponent(inviteBtn);
            return inviteButtonLayout;
        }
    }

    private void invite(User user){
        EventUser eventUser = eventUserService.insert(event.getPk(), user.getPk());
        event.getEventUsers().add(eventUser);
        usersToInviteGrid.getDataProvider().refreshItem(user);
    }

    private void uninvite(User user, Button uninviteBtn){
        EvntWebappUI.getCurrent().addWindow(
                new ConfirmDialog(
                        "Are you sure you want to remove "+user.getDisplayName()+" from the event?",
                        click -> {
                            eventUserService.delete(event.getPk(), user.getPk());
                            event.getEventUsers().remove(event.findUserOnEvent(user.getPk()));
                            usersToInviteGrid.getDataProvider().refreshItem(user);
                            uninviteBtn.setVisible(false);
                        },
                        click -> {}));
    }
}
