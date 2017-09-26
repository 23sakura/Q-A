package com.example.haruka.rescue_aid.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.haruka.rescue_aid.R;

/**
 * Created by Tomoya on 9/15/2017 AD.
 * This is the util to be used in any cases
 */

public class Utils {

    public final static String SCENARIOS_ILL = "ill/scenario_17091801.csv";
    public final static String SCENARIOS_INJURY = "injury/scenario_170918.csv";
    public final static String LIST_CARE = "carelist_v00.csv";

    public final static String TAG_CARE = "Care";
    public final static String TAG_END = "END";

    public final static String TAG_INTENT_CERTIFICATION = "CERTIFICATION";

    public static final long serialVersionUID_MedicalCertification = 1L;
    public static final long serialVersionUID_Question = 2L;

    public static final String ANSWER_YES = "YES";
    public static final String ANSWER_NO = "NO";
    public static final String ANSWER_SHORT_YES = "Y";
    public static final String ANSWER_SHORT_NO = "N";
    public static final String ANSWER_JP_YES = "はい";
    public static final String ANSWER_JP_NO = "いいえ";


    public final static int MIN_URGNECY = 1, MAX_URGENCY = 3;
    public static int[] URGENCY_COLORS = {0, Color.GREEN, Color.argb(255, 255, 165, 000), Color.RED};
    public static Drawable[] URGENCY_STYLE = {};
    public static String[] URGENCY_WARNING = {"", "大きな問題はありません", "医療機関の受診が必要です", "緊急度が高いです"};
    public final static int NUM_CARE = 7;

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

    public static int getXMLID(String xml){
        if ("care_aed".equals(xml)){
            return R.xml.care_aed;
        } else if ("care_chest_compression".equals(xml)){
            return R.xml.care_chest_compression;
        } else if ("care_bleed_stopping".equals(xml)) {
            return R.xml.care_bleed_stopping;
        } else if ("care_airway_foreign_body_removal.xml".equals(xml)) {
            return R.xml.care_airway_foreign_body_removal;
        } else if ("care_heatstroke.xml".equals(xml)){
            return R.xml.care_heatstroke;
        } else {
            return R.xml.care_recovery_position;
        }
    }

    public static String getDMSLocation(double degree){
        degree += 1.0/3600/2;
        int d = (int)degree;
        int m = (int)((degree - d) * 60);
        int s = (int)((degree - d - m/60.0)*3600);

        return Integer.toString(d) + "度" + Integer.toString(m) + "分" + Integer.toString(s) + "秒";
    }
}
