package mailapi.rest.controller;

import lombok.extern.slf4j.Slf4j;
import mailapi.domain.MailParams;
import mailapi.rest.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(MailParams mailParams){
        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setFrom(mailParams.getFrom());
            message.setTo(mailParams.getTo());
            message.setSubject(mailParams.getSubject());
            message.setText(mailParams.getBody(), true);
        });
    }
}
