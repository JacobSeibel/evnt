package com.evnt.ui.views;

import com.evnt.domain.SecurityRole;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Secured(SecurityRole.ROLE_ADMIN)
@SpringView(name = AdminView.NAME)
public class AdminView extends AbstractView {

    public static final String NAME = "admin";

    public AdminView() {
        addComponent(new Label("<h1>Admin Area</h1>", ContentMode.HTML));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
