package com.evnt.ui.components;

import com.evnt.domain.EventObject;
import com.evnt.domain.EventUser;
import com.evnt.domain.User;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.vaadin.ui.*;

public class ManageInvitesOverlay extends Window {

    private final UserDelegateService userService;
    private final EventDelegateService eventService;

    private final EventObject event;

    public ManageInvitesOverlay(EventObject event, UserDelegateService userService, EventDelegateService eventService){
        this.event = event;
        this.userService = userService;
        this.eventService = eventService;
        build();
        center();
        setClosable(true);
        setModal(true);
        setHeight("40%");
        setWidth("40%");
    }

    private void build(){
        VerticalLayout content = new VerticalLayout();
        content.setCaption("Manage Invites");
        Grid<User> usersToInviteGrid = new Grid<>();
        usersToInviteGrid.setItems(userService.findActive());

        usersToInviteGrid.addColumn(User::getDisplayName).setCaption("Name");
        usersToInviteGrid.addColumn(User::getUsername).setCaption("Username");
        usersToInviteGrid.addColumn(User::getEmail).setCaption("Email");
        usersToInviteGrid.addComponentColumn(user -> {
            EventUser eventUser = event.findUserOnEvent(user.getPk());
            if(eventUser != null){
                return new Label(eventUser.getResponse() != null ? eventUser.getResponse().getName() : "Invited");
            }else{
                HorizontalLayout inviteButtonLayout = new HorizontalLayout();
                Button inviteBtn = new Button("Invite");
                Label invitedLabel = new Label("Invited");
                invitedLabel.setVisible(false);
                inviteBtn.addClickListener(click -> {
                    invite(user);
                    click.getButton().setVisible(false);
                    invitedLabel.setVisible(true);
                });
                inviteButtonLayout.addComponents(inviteBtn, invitedLabel);
                return inviteButtonLayout;
            }
        });

        usersToInviteGrid.setSizeFull();
        content.addComponent(usersToInviteGrid);
        content.setSizeFull();
//        content.setMargin(true);
        setContent(content);
    }

    private void invite(User user){
        eventService.invite(event.getPk(), user.getPk());
    }
}
