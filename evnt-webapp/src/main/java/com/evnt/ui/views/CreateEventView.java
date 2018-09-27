package com.evnt.ui.views;

import com.evnt.domain.*;
import com.evnt.persistence.EventDelegateService;
import com.evnt.persistence.QueuedEmailDelegateService;
import com.evnt.persistence.UserDelegateService;
import com.evnt.spring.security.UserAuthenticationService;
import com.evnt.ui.EvntWebappUI;
import com.evnt.util.ParamUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.liveimageeditor.LiveImageEditor;

import java.io.*;
import java.nio.file.Files;
import java.time.ZoneId;
import java.util.Calendar;

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
    private QueuedEmailDelegateService queuedEmailService;
    @Autowired
    private UserAuthenticationService userAuthenticationService;

    private EventObject eventObject;
    private boolean isEdit;

    private void build() {
        if(eventObject.getPk() != null) {
            EventUser thisUserOnEvent = eventObject.findUserOnEvent(userAuthenticationService.loggedInUser().getPk());
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
        Image oldImage = new Image();
        oldImage.setWidth("100%");
        if(isEdit){
            oldImage.setSource(
                    new StreamResource((StreamResource.StreamSource) () ->
                            new ByteArrayInputStream(eventObject.getEventPhoto()), eventObject.getName()));
        }

        class ImageReceiver implements LiveImageEditor.ImageReceiver {
            @Getter
            InputStream inputStream;

            @Override
            public void receiveImage(InputStream inputStream) {
                this.inputStream = inputStream;
            }
        }

        ImageReceiver imageReceiver = new ImageReceiver();

        LiveImageEditor imageEditor = new LiveImageEditor(imageReceiver);
        imageEditor.setWidth("100%");
        imageEditor.setHeight("400px");
        VerticalLayout editorLayout = new VerticalLayout(imageEditor);
        editorLayout.setVisible(false);
        editorLayout.addLayoutClickListener(click -> imageEditor.requestEditedImage());
        editorLayout.setMargin(false);

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
                try {
                    editorLayout.setVisible(true);
                    imageEditor.setImage(Files.readAllBytes(file.toPath()));
                    imageEditor.resetTransformations();
                } catch (final java.io.IOException e) {
                    new Notification("Could not upload file!",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                }
            }
        }

        ImageUploader receiver = new ImageUploader();

        // Create the upload with a caption and set receiver later
        Upload upload = new Upload("Event Photo", receiver);
        if(isEdit) upload.setButtonCaption("Change");
        upload.addSucceededListener(receiver);
        upload.addSucceededListener(success -> {
            try {
                if(isEdit) oldImage.setVisible(false);
                imageEditor.setVisible(true);
                imageEditor.setImage(Files.readAllBytes(receiver.file.toPath()));
                imageEditor.resetTransformations();
                upload.setButtonCaption("Change");
                if(!receiver.file.delete()) throw new IOException("Failed to Delete");
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
        descriptionRichTextArea.setHeight("40em");
        descriptionRichTextArea.setWidth("100%");
        descriptionRichTextArea.focus();
        binder.forField(descriptionRichTextArea)
                .bind(EventObject::getDescription, EventObject::setDescription);
        CheckBox allowMaybesCheckbox = new CheckBox("Allow Maybes");
        allowMaybesCheckbox.setValue(true);
        binder.forField(allowMaybesCheckbox)
                .bind(EventObject::isAllowMaybes, EventObject::setAllowMaybes);
        DateField rsvpDeadlineDate = new DateField("RSVP Deadline");
        binder.forField(rsvpDeadlineDate)
                .withValidator(rsvpDate -> rsvpDate == null || rsvpDate.isBefore(startTimeField.getValue().toLocalDate()), "RSVP Deadline must be before Start Time!")
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(EventObject::getRsvpDate, EventObject::setRsvpDate);

        Button createEventButton = new Button(isEdit ? "Update Event" : "Create Event");
        createEventButton.setEnabled(isEdit);
        createEventButton.addClickListener(click -> {
            binder.writeBeanIfValid(eventObject);
            try {
                imageEditor.requestEditedImage();
                ByteArrayInputStream bais = (ByteArrayInputStream)imageReceiver.getInputStream();
                if(bais != null) {
                    byte[] array = new byte[bais.available()];
                    bais.read(array);
                    eventObject.setEventPhoto(array);
                }
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
        if(isEdit) addComponent(oldImage);
        addComponent(editorLayout);
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
        if(isEdit) notifyInviteesOfUpdate();
        EvntWebappUI.getUiService().postNavigationEvent(this, ViewEventView.NAME+"/?eventPk="+eventObject.getPk());
    }

    private void notifyInviteesOfUpdate(){
        //Send updates 10 minutes from now
        Calendar sendDateCalendar = Calendar.getInstance();
        sendDateCalendar.add(Calendar.MINUTE, 10);
        Email email = new Email(Email.EVENT_UPDATED);

        for(EventUser eu : eventObject.getEventUsers()){
            QueuedEmail qe = new QueuedEmail(
                    email,
                    eu.getUser(),
                    eventObject,
                    userAuthenticationService.loggedInUser(),
                    sendDateCalendar.getTime());

            queuedEmailService.insert(qe);
        }
    }



    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Integer eventPk = ParamUtils.getIntegerParam("eventPk");
        isEdit = eventPk != null;
        eventObject = isEdit ? eventService.findByPk(eventPk) : new EventObject();

        build();
    }
}
