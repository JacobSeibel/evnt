package com.evnt.ui.components;

import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.views.LogoutView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;

public class LogoutLink extends CustomComponent {

    public LogoutLink() {
        Link logoutLink = new Link("Logout", new ExternalResource("#!" + LogoutView.NAME));
        logoutLink.setIcon(VaadinIcons.SIGN_OUT);
        setCompositionRoot(logoutLink);
    }

    public void updateVisibility() {
        setVisible(!EvntWebappUI.getCurrent().isUserAnonymous());
    }
}
