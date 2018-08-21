package com.evnt.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "login";

    @PostConstruct
    void init() {
        addComponent(new Label("This is the default view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
