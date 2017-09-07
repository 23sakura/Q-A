package com.example.haruka.rescue_aid.utils;

import java.io.Serializable;

/**
 * Created by Tomoya on 9/5/2017 AD.
 */

public class Record implements Serializable {

    private static final long serialVersionUID = 2L;

    String time;
    String tag;
    String value;

    public Record(String tag, String value){
        time = QADateFormat.getInstance();
        this.tag = tag;
        this.value = value;
    }

    public String getTime(){
        return time;
    }

    public String getTagValue(){
        return tag + "," + value;
    }

    public String _toString(){
        return "Record:" + time + "," + tag + "," + value;
    }

    public String toString(){
        return time + "," + tag + "," + value;
    }
}

