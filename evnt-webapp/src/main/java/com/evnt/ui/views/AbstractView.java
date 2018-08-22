package com.evnt.ui.views;

import com.evnt.ui.EvntWebappUI;
import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

public abstract class AbstractView extends Panel implements View {
    private Logger LOG = LoggerFactory.getLogger(AbstractView.class);

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
        LOG.debug("About to destroy {}", getClass().getName());
        EvntWebappUI.getCurrent().getEventbus().unregister(this);
    }
}
