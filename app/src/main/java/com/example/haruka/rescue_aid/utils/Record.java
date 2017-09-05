package com.example.haruka.rescue_aid.utils;

/**
 * Created by Tomoya on 9/5/2017 AD.
 */

public class Record{
    String time;
    String tag;
    String value;

    public Record(String tag, String value){
        time = QADateFormat.getInstance();
        this.tag = tag;
        this.value = value;
    }

    public String toString(){
        return "Record : " + time + ", " + tag + ", " + value;
    }
}

