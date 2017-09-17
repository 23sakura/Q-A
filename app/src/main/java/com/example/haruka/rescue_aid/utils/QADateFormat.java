package com.example.haruka.rescue_aid.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Tomoya on 8/29/2017 AD.
 */

public class QADateFormat {

    public final static String simpleDateFormat =  "yyyy.MM.dd kk:mm:ss";

    public static String getInstance(){
        Date date = new Date();
        return getStringDate(date);
    }

    public static String getStringDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
        return sdf.format(date);
    }

    public static Date getDate(String dateFormat) {
        DateFormat df = new SimpleDateFormat(simpleDateFormat);
        Date date = null;
        try {
            date = df.parse(dateFormat);
        } catch (Exception e) {
            date = new Date();
        }
        return date;
    }
}
