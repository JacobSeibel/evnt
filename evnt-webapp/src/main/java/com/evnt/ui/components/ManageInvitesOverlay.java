package com.evnt.ui.components;

import com.evnt.domain.EventObject;
import com.evnt.domain.EventUser;
import com.evnt.domain.Role;
import com.evnt.domain.User;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.EventUserDelegateService;
import com.evnt.persistence.RoleDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.views.ViewEventView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import java.util.List;
import java.util.Set;


public class ManageInvitesOverlay extends Window {

    private final UserDelegateService userService;
    private final EventUserDelegateService eventUserService;
    private final RoleDelegateService roleService;

    private final EventObject event;
    private final boolean isHost;
    private Grid<User> usersToInviteGrid;

    public ManageInvitesOverlay(EventObject event, boolean isHost, UserDelegateService userService, EventUserDelegateService eventUserService, RoleDelegateService roleService){
        this.event = event;
        this.isHost = isHost;
        this.userService = userService;
        this.eventUserService = eventUserService;
        this.roleService = roleService;
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
        if(isHost) {
            HorizontalLayout adminButtons = new HorizontalLayout();
            Button setHosts = new Button("Set as Host");
            setHosts.addClickListener(click -> setHosts());
            Button removeHosts = new Button("Remove as Host");
            removeHosts.addClickListener(click -> removeHosts());
            adminButtons.addComponents(setHosts, removeHosts);
            content.addComponent(adminButtons);
        }

        usersToInviteGrid = getGrid();
        content.addComponent(usersToInviteGrid);
        setContent(content);
    }

    private void setHosts(){
        Set<User> selectedUsers = usersToInviteGrid.getSelectedItems();
        for(User user : selectedUsers){
            EventUser eventUser = event.findUserOnEvent(user.getPk());
            if(eventUser == null){
                eventUser = invite(user);
            }
            if (!Role.isHost(eventUser.getRole()) && !Role.isCreator(eventUser.getRole())) {
                eventUser.changeRole(roleService.findByPk(Role.HOST));
                eventUserService.update(eventUser);
            }
        }
        usersToInviteGrid.getDataProvider().refreshAll();
        usersToInviteGrid.deselectAll();
    }

    private void removeHosts(){
        Set<User> selectedUsers = usersToInviteGrid.getSelectedItems();
        for(User user : selectedUsers){
            EventUser eventUser = event.findUserOnEvent(user.getPk());
            if (Role.isHost(eventUser.getRole())) {
                eventUser.changeRole(roleService.findByPk(Role.GUEST));
                eventUserService.update(eventUser);
            }
        }
        usersToInviteGrid.getDataProvider().refreshAll();
        usersToInviteGrid.deselectAll();
    }

    private Grid<User> getGrid(){
        Grid<User> usersToInviteGrid = new Grid<>();
        usersToInviteGrid.setItems(userService.findActive());

        //TODO: Make sure invited users sort first, then uninvited users last
        usersToInviteGrid.addColumn(user -> {
            String displayName = user.getDisplayName();
            EventUser eventUser = event.findUserOnEvent(user.getPk());
            if(eventUser != null) displayName += Role.isCreator(eventUser.getRole()) || Role.isHost(eventUser.getRole()) ? " ("+eventUser.getRole().getName()+")" : "";
            return displayName;
        }).setCaption("Name");
        usersToInviteGrid.addColumn(User::getUsername).setCaption("Username");
        usersToInviteGrid.addColumn(User::getEmail).setCaption("Email");
        usersToInviteGrid.addComponentColumn(this::getResponseLayout);

        if(isHost) {
            usersToInviteGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        }

        usersToInviteGrid.setSizeFull();
        return usersToInviteGrid;
    }

    private HorizontalLayout getResponseLayout(User user){
        EventUser eventUser = event.findUserOnEvent(user.getPk());
        if(eventUser != null) {
            HorizontalLayout invitedLayout = new HorizontalLayout();
            String invitedString = eventUser.getResponse() != null ? eventUser.getResponse().getName() : "Invited";
            if(!Role.isGuest(eventUser.getRole())) invitedString += " ("+eventUser.getRole().getName()+")";
            Label invitedLabel = new Label(invitedString);
            invitedLabel.setWidth("10em");
            invitedLayout.addComponent(invitedLabel);
            if(!Role.isCreator(eventUser.getRole())) {
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

    private EventUser invite(User user){
        EventUser eventUser = eventUserService.insert(event.getPk(), user.getPk());
        event.getEventUsers().add(eventUser);
        usersToInviteGrid.getDataProvider().refreshItem(user);
        return eventUser;
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
