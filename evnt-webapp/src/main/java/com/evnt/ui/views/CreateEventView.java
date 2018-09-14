package com.evnt.ui.views;

import com.evnt.domain.*;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.ui.Theme;
import com.evnt.ui.components.RepositionableImage;
import com.evnt.util.ParamUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.liveimageeditor.LiveImageEditor;

import java.io.*;
import java.nio.file.Files;
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
    @Autowired
    private UserAuthenticationService userAuthService;

    private EventObject eventObject;
    private boolean isEdit;

    private void build() {
        if(eventObject != null) {
            EventUser thisUserOnEvent = eventObject.findUserOnEvent(userAuthService.loggedInUser().getPk());
            if (thisUserOnEvent == null || !(Role.isCreator(thisUserOnEvent.getRole()) || Role.isHost(thisUserOnEvent.getRole()))) {
                EvntWebappUI.getUiService().postNavigationEvent(this, AccessDeniedView.NAME);
                return;
            }
        }

        Binder<EventObject> binder = new Binder<>();
        binder.setBean(eventObject);

        TextField titleField = new TextField("Title");
        binder.forField(titleField)
                .asRequired("Title is required!")
                .bind(EventObject::getName, EventObject::setName);
        Image editedImage = new Image();
        RepositionableImage image = new RepositionableImage(isEdit);


        // Implement both receiver that saves upload in a file and
        // listener for successful upload
        class ImageUploader implements Upload.Receiver, Upload.SucceededListener{
            public File file;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                // Create upload stream
                FileOutputStream fos; // Stream to write to
                try {
                    // Open the file for writing.
                    file = new File(filename);
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {
                // Show the uploaded file in the image viewer
                image.getImage().setSource(new FileResource(file));
            }
        }
        ImageUploader receiver = new ImageUploader();
        LiveImageEditor imageEditor = new LiveImageEditor(new LiveImageEditor.ImageReceiver() {
            @Override
            public void receiveImage(InputStream inputStream) {
                StreamResource resource = new StreamResource(() -> inputStream, "edited-image-" + System.currentTimeMillis());
                editedImage.setSource(resource);
            }
        });
        imageEditor.setWidth("100%");
        imageEditor.setHeight("400px");
        imageEditor.setVisible(false);


        // Create the upload with a caption and set receiver later
        Upload upload = new Upload("Event Photo", receiver);
        upload.addSucceededListener(receiver);
        upload.addSucceededListener(success -> {
            try {
                imageEditor.setVisible(true);
                imageEditor.setImage(Files.readAllBytes(receiver.file.toPath()));
                imageEditor.resetTransformations();
                image.setEditable(true);
            } catch (final java.io.IOException e) {
                new Notification("Could not upload file!",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
            }
        });

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

        Button createEventButton = new Button(isEdit ? "Update Event" : "Create Event");
        createEventButton.setEnabled(isEdit);
        createEventButton.addClickListener(click -> {
            binder.writeBeanIfValid(eventObject);
            try {
                PositionedImage posImage = new PositionedImage(Files.readAllBytes(receiver.file.toPath()), image.getScrollTop(), image.getScrollLeft());
                eventObject.setEventPhoto(posImage);
            } catch (final java.io.IOException e) {
                new Notification("Could not upload file!",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return;
            }
            createEvent();
        });
        binder.addValueChangeListener(change -> createEventButton.setEnabled(binder.isValid()));

        addComponent(titleField);
//        addComponent(image);
        addComponent(imageEditor);
        addComponent(upload);
        addComponent(locationField);
        addComponent(startTimeField);
        addComponent(endTimeField);
        addComponent(descriptionRichTextArea);
        addComponent(allowMaybesCheckbox);
        addComponent(rsvpDeadlineDate);
        addComponent(createEventButton);
    }

    private void createEvent(){
        eventObject = isEdit ? eventService.update(eventObject) : eventService.insert(eventObject);
        Notification success = new Notification("Successfully "+ (isEdit ? "updated" : "created") + " event!");
        success.setDelayMsec(3000);
        success.show(Page.getCurrent());
        if(isEdit) notifyInvitees();
        EvntWebappUI.getUiService().postNavigationEvent(this, ViewEventView.NAME+"/?eventPk="+eventObject.getPk());
    }

    private void notifyInvitees(){
        //TODO: Notify invitees that the event has been updated
    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Integer eventPk = ParamUtils.getIntegerParam("eventPk");
        eventObject = eventPk != null ? eventService.findByPk(eventPk) : null;
        isEdit = eventObject != null;

        build();
    }
}
