package com.evnt.ui.views;

import com.evnt.ui.EvntWebappUI;
import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

@Slf4j
public abstract class AbstractView extends Panel implements View {
    private VerticalLayout layout;

    public AbstractView() {
        setSizeFull();
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }

    public void addComponent(Component c) {
        layout.addComponent(c);
    }

    protected void registerWithEventbus() {
        EvntWebappUI.getCurrent().getEventbus().register(this);
    }

    @PreDestroy
    public void destroy() {
        log.debug("About to destroy {}", getClass().getName());
        EvntWebappUI.getCurrent().getEventbus().unregister(this);
    }
}
