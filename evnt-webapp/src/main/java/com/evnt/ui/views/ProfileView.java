package com.evnt.ui.views;

import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.components.GoToMainViewLink;
import com.evnt.ui.components.LogoutLink;
import com.evnt.ui.events.UserLoggedInEvent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;

@Secured("ROLE_USER")
@SpringView(name = ProfileView.NAME)
public class ProfileView extends AbstractView {

    @Autowired
    UserAuthenticationService userAuthenticationService;

    private Logger LOG = LoggerFactory.getLogger(ProfileView.class);

    public final static String NAME = "profile";

    private String labelProperty;
    private LogoutLink logoutLink;

    public ProfileView() {
        logoutLink = new LogoutLink();
        labelProperty = "";
        addComponent(new Label(labelProperty, ContentMode.HTML));
        updateLabelProperty();
        addComponent(new GoToMainViewLink());
        addComponent(logoutLink);
        logoutLink.updateVisibility();

    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("Created new instance of ProfileView");
    }

    private void updateLabelProperty() {
        labelProperty= "<h1>"
                + (EvntWebappUI.getCurrent().getCurrentUser() == null ? "" : EvntWebappUI.getCurrent().getCurrentUser().getFullName())
                + "'s Profile</h1>... not much to see here, though.";
    }

    @Override
    public void enter(ViewChangeEvent event) {
        LOG.info("Entering profile view");
    }

    @Subscribe
    public void userLoggedIn(UserLoggedInEvent event) {
        logoutLink.updateVisibility();
        updateLabelProperty();
    }
}
