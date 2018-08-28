package com.evnt.ui.views;

import com.evnt.persistence.AdminService;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Secured("ROLE_ADMIN")
@SpringView(name = AdminView.NAME)
public class AdminView extends AbstractView {

    public static final String NAME = "admin";

    public AdminView(@Autowired AdminService adminService) {
        super(adminService);
        addComponent(new Label("<h1>Admin Area</h1>", ContentMode.HTML));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
