package com.evnt.ui.components;

import com.evnt.domain.*;
import com.evnt.domain.EventObject;
import com.evnt.persistence.*;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

import java.util.*;


public class ManageInvitesOverlay extends Window {

    private final UserDelegateService userService;
    private final EventUserDelegateService eventUserService;
    private final RoleDelegateService roleService;
    private final QueuedEmailDelegateService queuedEmailService;
    private final UserAuthenticationService userAuthService;

    private final EventObject event;
    private final boolean isHost;
    private Grid<User> usersToInviteGrid;
    private List<EventUser> usersAlreadyInvited;

    public ManageInvitesOverlay(
            EventObject event,
            boolean isHost,
            UserDelegateService userService,
            EventUserDelegateService eventUserService,
            RoleDelegateService roleService,
            QueuedEmailDelegateService queuedEmailService,
            UserAuthenticationService userAuthService){
        this.event = event;
        this.isHost = isHost;
        this.userService = userService;
        this.eventUserService = eventUserService;
        this.roleService = roleService;
        this.queuedEmailService = queuedEmailService;
        this.userAuthService = userAuthService;
        build();
        center();
        setClosable(true);
        setModal(true);
        setHeight("60%");
        setWidth("40%");

        addCloseListener(close -> notifyUsers());
    }

    private void build(){
        usersAlreadyInvited = new ArrayList<>(event.getEventUsers());

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
        List<User> users = userService.findActive();
        users.sort((u1, u2) -> {
            EventUser u1OnEvent = event.findUserOnEvent(u1.getPk());
            EventUser u2OnEvent = event.findUserOnEvent(u2.getPk());
            if((u1OnEvent != null && u2OnEvent != null) || (u1OnEvent == null && u2OnEvent == null)){
                return u1.getDisplayName().toLowerCase().compareTo(u2.getDisplayName().toLowerCase());
            }else if(u1OnEvent == null){
                return -1;
            }else{
                return 1;
            }
        });
        usersToInviteGrid.setItems(users);

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
        EventUser eventUser = new EventUser();
        eventUser.setEventFk(event.getPk());
        eventUser.setRole(new Role(Role.GUEST));
        eventUser.setUser(user);
        eventUser = eventUserService.insert(eventUser);
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

    private void notifyUsers(){
        //Send invites 10 minutes from now
        Calendar sendDateCalendar = Calendar.getInstance();
        sendDateCalendar.add(Calendar.MINUTE, 10);

        Email invitation = new Email(Email.INVITATION);

        for(EventUser eu : event.getEventUsers()){
            if(!usersAlreadyInvited.contains(eu)){
                QueuedEmail email = new QueuedEmail(
                        invitation,
                        eu.getUser(),
                        event,
                        userAuthService.loggedInUser(),
                        sendDateCalendar.getTime()
                );

                queuedEmailService.insert(email);
            }
        }

        //Cancel unsent invites for uninvited people
        for(EventUser eu : usersAlreadyInvited){
            if(!event.getEventUsers().contains(eu)){
//                queuedEmailService.delete();
            }
        }
    }
}
