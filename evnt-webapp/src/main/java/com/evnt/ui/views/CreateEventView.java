package com.evnt.ui.views;

import com.evnt.domain.EventObject;
import com.evnt.domain.SecurityRole;
import com.evnt.domain.User;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.ui.EvntWebappUI;
import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Secured(SecurityRole.ROLE_USER)
@SpringView(name = CreateEventView.NAME)
public class CreateEventView extends AbstractView implements View {
    public static final String NAME = "create-event";

    @Autowired
    private UserDelegateService userService;
    @Autowired
    private EventDelegateService eventService;

    @PostConstruct
    void init() {
        Binder<EventObject> binder = new Binder<>();

        TextField titleField = new TextField("Title");
        binder.forField(titleField)
                .asRequired("Title is required!")
                .bind(EventObject::getName, EventObject::setName);
        TextField locationField = new TextField("Location");
        binder.forField(locationField)
                .bind(EventObject::getLocation, EventObject::setLocation);
        DateTimeField startTimeField = new DateTimeField("Start Time");
        binder.forField(startTimeField)
                .asRequired("Start Time is required!")
                .withConverter(new LocalDateTimeToDateConverter(ZoneId.systemDefault()))
                .bind(EventObject::getStartDate, EventObject::setStartDate);
        DateTimeField endTimeField = new DateTimeField("End Time");
        binder.forField(endTimeField)
                .withValidator(endTime -> endTime == null || endTime.isAfter(startTimeField.getValue()), "End Time must be after Start Time!")
                .withConverter(new LocalDateTimeToDateConverter(ZoneId.systemDefault()))
                .bind(EventObject::getEndDate, EventObject::setEndDate);
        RichTextArea descriptionRichTextArea = new RichTextArea("Description");
        binder.forField(descriptionRichTextArea)
                .bind(EventObject::getDescription, EventObject::setDescription);
        CheckBox allowMaybesCheckbox = new CheckBox("Allow Maybes");
        allowMaybesCheckbox.setValue(true);
        binder.forField(allowMaybesCheckbox)
                .bind(EventObject::isAllowMaybes, EventObject::setAllowMaybes);
        DateField rsvpDeadlineDate = new DateField("RSVP Deadline");
        binder.forField(rsvpDeadlineDate)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(EventObject::getRsvpDate, EventObject::setRsvpDate);

        Button createEventButton = new Button("Create Event");
        createEventButton.setEnabled(false);
        createEventButton.addClickListener(click -> {
            EventObject eventObject = new EventObject();
            binder.writeBeanIfValid(eventObject);
            createEvent(eventObject);
        });
        binder.addValueChangeListener(change -> createEventButton.setEnabled(binder.isValid()));

        addComponent(titleField);
        addComponent(locationField);
        addComponent(startTimeField);
        addComponent(endTimeField);
        addComponent(descriptionRichTextArea);
        addComponent(allowMaybesCheckbox);
        addComponent(rsvpDeadlineDate);
        addComponent(createEventButton);
    }

    private void createEvent(EventObject eventObject){
        eventService.insert(eventObject);
        Notification success = new Notification("Successfully created event!");
        success.setDelayMsec(3000);
        success.show(Page.getCurrent());
        EvntWebappUI.getUiService().postNavigationEvent(this, ViewEventView.NAME+"/?eventPk="+eventObject.getPk());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
