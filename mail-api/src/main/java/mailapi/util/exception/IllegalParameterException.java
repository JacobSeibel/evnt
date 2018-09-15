package mailapi.util.exception;

import mailapi.util.MailUtils;

public class IllegalParameterException extends RuntimeException{
    private String parameter;

    public IllegalParameterException(String parameter){
        super(MailUtils.EXCEPTION_ILLEGAL_PARAMETER + " : "+parameter);
        this.parameter = parameter;
    }
}
