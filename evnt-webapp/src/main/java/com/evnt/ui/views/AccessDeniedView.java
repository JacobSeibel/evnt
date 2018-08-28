package com.evnt.ui.views;

import com.evnt.persistence.AdminService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = AccessDeniedView.NAME)
public class AccessDeniedView extends AbstractView  {

    public static final String NAME = "accessDenied";

    public AccessDeniedView(@Autowired AdminService adminService) {
        super(adminService);
        addComponent(new Label("<h1>Access Denied!</h1>", ContentMode.HTML));
        addComponent(new Label("You don't have required permission to access this resource."));
        Link homeLink = new Link("Home", new ExternalResource("#"));
        homeLink.setIcon(VaadinIcons.HOME);
        addComponent(homeLink);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
