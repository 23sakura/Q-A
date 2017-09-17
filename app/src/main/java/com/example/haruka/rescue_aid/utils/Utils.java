package com.example.haruka.rescue_aid.utils;

import android.graphics.Color;

import com.example.haruka.rescue_aid.R;

/**
 * Created by Tomoya on 9/15/2017 AD.
 */

public class Utils {
    public final static int MIN_URGNECY = 1, MAX_URGENCY = 3;
    public static int[] URGENCY_COLORS = {0, Color.GREEN, Color.YELLOW, Color.RED};
    public static String[] URGENCY_WARNING = {"", "大きな問題はありません", "医療機関の受診が必要です", "緊急度が高いです"};
    public final static int NUM_CARE = 7;

    public static int getCareXmlID(String xmlName){
        int xmlID = 0;
        switch(xmlName){
            case "care_chest_compression":
                xmlID = R.xml.care_chest_compression;
                break;

            case "care_bleed_stopping":
                xmlID = R.xml.care_bleed_stopping;
                break;

            case "care_aed":
                xmlID = R.xml.care_aed;
                break;

            case "care_recovery_position":
                xmlID = R.xml.care_recovery_position;
                break;

            default:
                xmlID = -1;
        }

        return xmlID;
    }

    public static String getAnswerString(boolean answer){
        return (answer) ? "Y":"N";
    }

    public static boolean getAnswerBoolean(String answer){
        return "Y".equals(answer);
    }
}
