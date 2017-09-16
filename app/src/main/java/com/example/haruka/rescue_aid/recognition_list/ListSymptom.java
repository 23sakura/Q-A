package com.example.haruka.rescue_aid.recognition_list;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/16/2017 AD.
 */

public class ListSymptom {
    private static String[] ill = {
            "病気", "容器", " 陽気", "猟奇", "リョーキ", "妖気"
    };
    private static String[] injury = {
            "怪我", "気が", "けが", " ケガ", "ギガ", "ティーガー", "映画", "慶賀", "経過", "兄が", "giga"
    };

    public static ArrayList<String>[] getDictionary(){
        ArrayList<String>[] Dictionay = new ArrayList[2];
        Dictionay[0] = new ArrayList<String>();
        Dictionay[1] = new ArrayList<String>();

        for(String i : ill){
            Dictionay[0].add(i);
        }
        for(String i : injury){
            Dictionay[1].add(i);
        }

        return Dictionay;
    }
}
