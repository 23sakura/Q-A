package com.example.haruka.rescue_aid.recognition_list;

import java.util.ArrayList;

/**
 * Created by Tomoya on 8/23/2017 AD.
 */

public class YesClass {
    private static String[] yes = {
        "はい", "ハイ", "肺", "hi", "high", "はーい", "愛", "i", "yes", "es", "jesus", "is"
    };
    private static String[] no = {
      "いいえ", "イイエ", "家", "イエ", "言え", "No", "ノー", "脳", "能", "膿", "ea"
    };

    public static ArrayList<String>[] getDictionary(){
        ArrayList<String>[] Dictionay = new ArrayList[2];
        Dictionay[0] = new ArrayList<String>();
        Dictionay[1] = new ArrayList<String>();

        for(String y : yes){
            Dictionay[0].add(y);
        }
        for(String n : no){
            Dictionay[1].add(n);
        }

        return Dictionay;
    }
}
