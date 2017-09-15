package com.example.haruka.rescue_aid.utils;

/**
 * Created by Tomoya on 9/15/2017 AD.
 */

public class Care {

    public int index;
    public String name;
    public String xml;
    public static final String nullXML = "";

    public Care(int index, String name, String xml){
        this.index = index;
        this.name = name;
        this.xml = xml;
    }

    public String getXml(){
        if (xml.equals(nullXML)){
            return null;
        } else {
            return xml;
        }
    }

}
