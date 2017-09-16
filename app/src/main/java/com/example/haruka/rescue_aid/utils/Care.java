package com.example.haruka.rescue_aid.utils;

/**
 * Created by Tomoya on 9/15/2017 AD.
 */

public class Care {

    public int index;
    public String name;
    public String xml;
    public String description;
    public static final String NULL_XML = "";

    public Care(int index, String name, String xml){
        this.index = index;
        this.name = name;
        this.xml = xml;
        this.description = "";
    }

    public void setDescription(String text){
        description = text;
    }

    public String getXml(){
        if (xml.equals(NULL_XML)){
            return null;
        } else {
            return xml;
        }
    }



}
