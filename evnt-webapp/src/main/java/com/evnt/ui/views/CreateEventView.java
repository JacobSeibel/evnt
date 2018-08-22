package com.evnt.ui.views;

import com.evnt.domain.User;
import com.evnt.persistence.UserDelegateService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Slf4j
@SpringView(name = CreateEventView.VIEW_NAME)
public class CreateEventView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "create-event";

    @Autowired
    private UserDelegateService userService;

    @PostConstruct
    void init() {
        StringBuilder sb = new StringBuilder();
        for(User u : userService.findAll()){
            sb.append(u.getFirstName()).append(" ").append(u.getLastName());
        }
        addComponent(new Label(sb.toString()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
