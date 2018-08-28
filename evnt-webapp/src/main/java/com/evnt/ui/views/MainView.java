package com.evnt.ui.views;

import com.evnt.domain.User;
import com.evnt.persistence.AdminService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.components.LogoutLink;
import com.evnt.ui.events.LogoutEvent;
import com.evnt.ui.events.UserLoggedInEvent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringView(name = MainView.NAME)
public class MainView extends AbstractView {

    private UserDelegateService userService;

    public final static String NAME = "main";

    private String welcomeLabelText;

    public MainView(
            @Autowired AdminService adminService,
            @Autowired UserDelegateService userService
    ) {
        super(adminService);

        this.userService = userService;

        welcomeLabelText = "";

        updateWelcomeMessage();
        Label welcomeLabel = new Label(welcomeLabelText, ContentMode.HTML);

        addComponent(welcomeLabel);

        registerWithEventbus();
    }

    private void updateWelcomeMessage() {
        String username = null;
        if (!EvntWebappUI.getCurrent().isUserAnonymous()) {
            final User principal = userService.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            username = principal.getFullName();
        }

        welcomeLabelText = username == null ? "<h1>Welcome Stranger</h1><hr/>You're currently not logged in.<hr/>"
                        : "<h1>Welcome " + username + "!</h1><hr/>";
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    @Override
    public void userLoggedIn(UserLoggedInEvent event) {
        super.userLoggedIn(event);
        updateWelcomeMessage();
    }

    @Override
    public void userLoggedOut(LogoutEvent event) {
        super.userLoggedOut(event);
        updateWelcomeMessage();
    }
}