package com.example.haruka.rescue_aid.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Tomoya on 8/29/2017 AD.
 */

public class QADateFormat {

    public final static String simpleDateFormat =  "yyyy/MM/dd kk:mm:ss";

    public static String getInstance(){
        SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);

        Date date = new Date();
        return sdf.format(date);
    }

    public static Date getDate(String dateFormat) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = df.parse(dateFormat);
        } catch (Exception e) {
            date = new Date();
        }
        return date;
    }
}
