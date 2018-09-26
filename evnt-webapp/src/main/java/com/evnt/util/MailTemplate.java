package com.evnt.util;

import com.evnt.domain.MailParams;
import com.evnt.domain.QueuedEmail;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.text.StringSubstitutor;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class MailTemplate {
    public static MailTemplate INSTANCE = new MailTemplate();

    private static final String SENDER_NAME = "senderName";
    private static final String EVENT_NAME = "eventName";
    private static final String RSVP_RESPONSE = "rsvpResponse";

    private Configuration freemarkerConfig;

    public MailTemplate(){
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_28);
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
    }

    private StringWriter generateBody(String templateUri, Map<String, Object> data){
        StringWriter sw = new StringWriter();
        try {
            Template template = freemarkerConfig.getTemplate(templateUri);
            template.process(data, sw);
        } catch (Exception e){
            e.printStackTrace();
        }

        return sw;
    }

    public MailParams getEmail(QueuedEmail email){
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);

        StringWriter sw = generateBody(
                email.getEmail().getFreemarkerTemplate(),
                data);

        return MailParams.build(
                email.getRecipient().getEmail(),
                null,
                buildSubject(email),
                sw.toString()
        );
    }

    private String buildSubject(QueuedEmail email){
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put(SENDER_NAME, email.getSender().getDisplayName());
        valuesMap.put(EVENT_NAME, email.getEvent().getName());
        String subject = email.getEmail().getSubjectLine();
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        return sub.replace(subject);
    }
}
