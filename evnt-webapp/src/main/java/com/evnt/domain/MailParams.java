package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString @Getter @Setter
public class MailParams implements Serializable{
    private String to;
    private String from;
    private String subject;
    private String body;

    public static MailParams build(String to, String from, String subject, String body){
        MailParams mailParams = new MailParams();
        mailParams.setTo(to);
        mailParams.setFrom(from);
        mailParams.setSubject(subject);
        mailParams.setBody(body);

        return mailParams;
    }
}
