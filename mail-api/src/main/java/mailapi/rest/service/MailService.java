package mailapi.rest.service;


import mailapi.domain.MailParams;

public interface MailService {
    void sendMail(MailParams params);
}
