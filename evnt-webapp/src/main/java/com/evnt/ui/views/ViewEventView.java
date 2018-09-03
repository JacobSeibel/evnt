package com.evnt.ui.views;

import com.evnt.domain.EventObject;
import com.evnt.domain.SecurityRole;
import com.evnt.persistence.EventDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.util.DateUtils;
import com.evnt.util.ParamUtils;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.net.URI;

@Secured(SecurityRole.ROLE_USER)
@SpringView(name = ViewEventView.NAME)
public class ViewEventView extends AbstractView {

    @Autowired
    private UserAuthenticationService userAuthService;
    @Autowired
    private EventDelegateService eventService;

    public final static String NAME = "view-event";

    private EventObject event = null;

    private void build(){
        //TODO: Event Photo

        Label eventTitleLabel = new Label(event.getName());
        Label locationLabel = new Label(event.getLocation());
        locationLabel.setCaption("Location");
        Label startDateLabel = new Label(DateUtils.getPresentableDate(event.getStartDate()));
        startDateLabel.setCaption("Start Time");
        Label endDateLabel = new Label(DateUtils.getPresentableDate(event.getEndDate()));
        endDateLabel.setCaption("End Time");
        RichTextArea descriptionRichTextArea = new RichTextArea("Description");
        descriptionRichTextArea.setValue(event.getDescription());
        descriptionRichTextArea.setReadOnly(true);

        addComponent(eventTitleLabel);
        addComponent(locationLabel);
        addComponent(startDateLabel);
        addComponent(endDateLabel);
        addComponent(descriptionRichTextArea);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Integer eventPk = ParamUtils.getIntegerParam("eventPk");
        this.event = eventService.findByPk(eventPk);

        build();
    }
}