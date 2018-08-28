package com.evnt.ui.views;

import com.evnt.persistence.AdminService;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = AboutView.NAME)
public class AboutView extends AbstractView {

    public static final String NAME = "about";

    public AboutView(@Autowired AdminService adminService) {
        super(adminService);
        addComponent(new Label("<h1>About</h1>", ContentMode.HTML));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
