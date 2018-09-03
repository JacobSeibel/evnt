package com.evnt.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getPresentableDate(Date date){
        DateFormat df = new SimpleDateFormat("EEEEE, MMMM d y");

        return df.format(date);
    }
}
