package mailapi.util.exception;

import mailapi.util.MailUtils;

public class EmailSendException extends RuntimeException{
    public EmailSendException(){
        super(MailUtils.EXCEPTION_EMAIL_FAILED);
    }
}
