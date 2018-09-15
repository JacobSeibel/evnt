package com.evnt.util;

import com.evnt.domain.EventObject;
import com.evnt.domain.EventUser;
import com.evnt.domain.MailParams;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MailTemplate {
    public static MailTemplate INSTANCE = new MailTemplate();

    private Configuration freemarkerConfig;

    public MailTemplate(){
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_28);
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
    }

    private StringWriter generateBody(String templateUri, EventObject event, Map<String, Object> data){
        StringWriter sw = new StringWriter();
        try {
            Template template = freemarkerConfig.getTemplate(templateUri);
            data.put("event", event);

            template.process(data, sw);
        } catch (Exception e){
            e.printStackTrace();
        }

        return sw;
    }

    public MailParams getInvitedEmail(EventUser eventUser, String inviter, EventObject event){
        Map<String, Object> data = new HashMap<>();
        data.put("inviter", inviter);
        StringWriter sw = generateBody("invited.ftl", event, data);

        return MailParams.build(
                eventUser.getUser().getEmail(),
                null,
                inviter + " has invited you to " + event.getName(),
                sw.toString()
        );
    }

    public MailParams getRsvpReminderEmail(String toAddress, EventObject event){
        StringWriter sw = generateBody("rsvpReminder.ftl", event, new HashMap<>());

        return MailParams.build(
                toAddress,
                null,
                "Remember to RSVP to " + event.getName(),
                sw.toString()
        );
    }

    public MailParams getRsvpNotificationEmail(EventUser invitee, EventObject event){
        StringWriter sw = generateBody("rsvpNotification.ftl", event, new HashMap<>());

        return MailParams.build(
                invitee.getUser().getEmail(),
                null,
                invitee.getUser().getDisplayName() + " responded " + invitee.getResponse().getName() + " to " + event.getName(),
                sw.toString()
        );
    }

    public MailParams getEventUpdatedEmail(String toAddress, String updater, EventObject event){
        StringWriter sw = generateBody("eventUpdated.ftl", event, new HashMap<>());

        return MailParams.build(
                toAddress,
                null,
                updater + " has updated " + event.getName(),
                sw.toString()
        );
    }
}
