package mailapi.util;

import mailapi.util.exception.IllegalParameterException;
import org.springframework.util.StringUtils;

public class MailUtils {

    //Exception Labels
    public static final String EXCEPTION_ILLEGAL_PARAMETER = "Illegal parameter";
    public static final String EXCEPTION_EMAIL_FAILED = "Sending email failed";

    public static void validateParam(String paramName, String paramValue){
        if(!StringUtils.hasText(paramValue)) throw new IllegalParameterException(paramName);
    }
}
