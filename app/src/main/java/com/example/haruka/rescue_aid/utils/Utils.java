package com.example.haruka.rescue_aid.utils;

import android.graphics.Color;
import android.util.Log;

import com.example.haruka.rescue_aid.R;

/**
 * Created by Tomoya on 9/15/2017 AD.
 */

public class Utils {

    public final static String SCENARIOS_ILL = "ill/scenario_17091701.csv";
    public final static String SCENARIOS_INJURY = "injury/scenario_17091501.csv";

    public static final long serialVersionUID_MedicalCertification = 1L;
    public static final long serialVersionUID_Question = 2L;

    public static final String ANSWER_YES = "YES";
    public static final String ANSWER_NO = "NO";
    public static final String ANSWER_SHORT_YES = "Y";
    public static final String ANSWER_SHORT_NO = "N";
    public static final String ANSWER_JP_YES = "はい";
    public static final String ANSWER_JP_NO = "いいえ";


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

    public static String getScenario(int scenarioID){
        switch (scenarioID){
            case 0:
                Log.d("return", SCENARIOS_ILL);
                return SCENARIOS_ILL;
            case 1:
                Log.d("return", SCENARIOS_INJURY);
                return SCENARIOS_INJURY;
            default:
                return SCENARIOS_ILL;
        }
    }
}
