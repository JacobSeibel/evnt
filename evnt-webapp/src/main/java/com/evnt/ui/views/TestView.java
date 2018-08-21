package com.evnt.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

@SpringView(name=TestView.VIEW_NAME)
public class TestView extends VerticalLayout implements View{
    public static final String VIEW_NAME = "test";

    @PostConstruct
    void init() {
        addComponent(new Label("This is the test view"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
