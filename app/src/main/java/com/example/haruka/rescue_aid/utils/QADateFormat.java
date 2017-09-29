package com.example.haruka.rescue_aid.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Tomoya on 8/29/2017 AD.
 * This class is the date format
 */

public class QADateFormat {

    public final static String simpleDateFormat =  "yyyy.MM.dd kk:mm:ss";
    public final static String simpleDateFormat2 =  "yyyy.MM.dd kk.mm.ss";
    public final static String JapaneseDateFormat =  "yyyy年MM月dd日 kk時mm分";
    public final static String FileDateFormat = "yyyyMMddkkmmss";
    public final static String FileDateFormat2 = "MM/dd kk:mm";

    public static String getInstance(){
        Date date = new Date();
        return getStringDate(date);
    }

    public static String getInstanceJapanese(){
        Date date = new Date();
        return getStringDateJapanese(date);
    }

    public static String getInstanceFilename(){
        Date date = new Date();
        return getStringDateFilename(date);
    }

    public static String getStringDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(simpleDateFormat);
        return sdf.format(date);
    }

    public static String getStringDateJapanese(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(JapaneseDateFormat);
        return sdf.format(date);
    }

    public static String getStringDateFilename(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FileDateFormat);
        return sdf.format(date);
    }

    public static String getStringDateFilename2(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FileDateFormat2);
        return sdf.format(date);
    }


    public static Date getDate(String dateFormat) {
        DateFormat df = new SimpleDateFormat(simpleDateFormat);
        Date date = null;
        try {
            Log.d("Date format", dateFormat);
            date = df.parse(dateFormat);
        } catch (Exception e) {
            Log.e("Date format", e.toString());
            date = new Date();
        }
        return date;
    }

}
