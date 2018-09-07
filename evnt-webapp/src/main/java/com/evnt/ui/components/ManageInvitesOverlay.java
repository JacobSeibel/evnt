package com.evnt.ui.components;

import com.evnt.domain.EventObject;
import com.evnt.domain.EventUser;
import com.evnt.domain.User;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.views.ViewEventView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.List;

public class ManageInvitesOverlay extends Window {

    private final UserDelegateService userService;
    private final EventDelegateService eventService;

    private final EventObject event;
    private Grid<User> usersToInviteGrid;

    public ManageInvitesOverlay(EventObject event, UserDelegateService userService, EventDelegateService eventService){
        this.event = event;
        this.userService = userService;
        this.eventService = eventService;
        build();
        center();
        setClosable(true);
        setModal(true);
        setHeight("60%");
        setWidth("40%");

        this.addCloseListener(close -> EvntWebappUI.getUiService().postNavigationEvent(this, ViewEventView.NAME+"/?eventPk="+event.getPk()));
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
            Label invitedLabel = new Label(eventUser.getResponse() != null ? eventUser.getResponse().getName() : "Invited");
            Button uninviteBtn = new Button();
            uninviteBtn.setIcon(VaadinIcons.CLOSE);
            uninviteBtn.addClickListener(click -> {
                uninvite(user);
                uninviteBtn.setVisible(false);
            });

            invitedLayout.addComponents(invitedLabel, uninviteBtn);

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
        eventService.invite(event.getPk(), user.getPk());
        refreshGrid();
    }

    private void uninvite(User user){
        EvntWebappUI.getCurrent().addWindow(
                new ConfirmDialog(
                        "Are you sure you want to remove "+user.getDisplayName()+" from the event?",
                        click -> {
                            eventService.uninvite(event.getPk(), user.getPk());
                            refreshGrid();
                        },
                        click -> {}));
    }

    private void refreshGrid(){
        event.setEventUsers(eventService.findEventUsersByPk(event.getPk()));
        usersToInviteGrid.getDataProvider().refreshAll();
    }


}
