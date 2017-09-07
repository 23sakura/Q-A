package com.example.haruka.rescue_aid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.haruka.rescue_aid.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/1/2017 AD.
 */

public class EmergencyExplanation {

    public String title;
    private AssetManager assetManager;
    private ArrayList<EmergencySituation> explain;
    public boolean isMetronomeRequired;

    public int id;
    public int numSituation;

    public String sub;
    public boolean isActive;

    public EmergencyExplanation(Context context, String situation){
        isActive = true;
        sub = "";
        loadXML(context, situation);

    }

    public void loadXML(Context context, String situation){
        int xmlID = 0;
        title = "";
        switch(situation){
            case "care_chest_compression":
                xmlID = R.xml.care_chest_compression;
                title = "care_chest_compression";
                break;

            case "care_aed":
                xmlID = R.xml.care_aed;
                title = "care_aed";
                break;

            case "care_recovery_position":
                title = "recovry_position";
                xmlID = R.xml.care_recovery_position;
                break;

            default:
                isActive = false;
                return;
        }
        assetManager = context.getResources().getAssets();
        XmlResourceParser xpp = context.getResources().getXml(xmlID);
        EmergencySituation emergencySituation = null;
        isMetronomeRequired = false;

        try{
            int eventType = xpp.getEventType();
            while(eventType != XmlResourceParser.END_DOCUMENT){
                final String name = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("tag", "Start Document");

                        break;

                    case XmlPullParser.START_TAG:
                        if ("id".equals(name)) {
                            Log.d("tag", "id");
                            id = Integer.parseInt(xpp.nextText());
                        } else if ("num".equals(name)){
                            numSituation = Integer.parseInt(xpp.nextText());
                        } else if ("items".equals(name)) {
                            Log.d("tag", "items");
                            explain = new ArrayList();
                        } else if ("item".equals(name)) {
                            Log.d("tag", "item");
                            emergencySituation = new EmergencySituation();
                        } else if ("description".equals(name)) {
                            Log.d("tag", "description");
                            emergencySituation.text = xpp.nextText();
                        } else if ("image".equals(name)){
                            Log.d("tag", "image");
                            try {
                                String filename = xpp.nextText();
                                Drawable drawable = Drawable.createFromStream(assetManager.open(filename.trim()), null);
                                emergencySituation.drawable = drawable;
                            }catch (Exception e){
                                Log.e("Emergency", e.toString());
                            }
                        } else if ("duration".equals(name)) {
                            Log.d("tag", "duration");
                            emergencySituation.duration = Integer.parseInt(xpp.nextText());
                        } else if ("button".equals(name)){
                            Log.d("tag", "button");
                            emergencySituation.button = xpp.nextText();
                        } else if ("button2".equals(name)){
                            Log.d("tag", "button2");
                            emergencySituation.button2 = xpp.nextText();
                        } else if ("metronome".equals(name)){
                            if (Integer.parseInt(xpp.nextText()) == 1) {
                                isMetronomeRequired = true;
                            }
                        } else if ("sub".equals(name)){
                            sub = xpp.nextText().trim();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(name)) {
                            Log.d("tag", "item End");
                            explain.add(emergencySituation);
                        } else if ("items".equals(name)) {
                            Log.d("tag", "items End");
                            explain.add(emergencySituation);

                        }
                        break;
                    default:
                        break;
                }

                eventType = xpp.next();
            }

        }catch (Exception e){

        }
    }

    public String getText(int id){
        return explain.get(id).text;
    }

    public Drawable getImage(int id){
        return explain.get(id).drawable;
    }

    public int getProcesses(){
        return explain.size();
    }

    public int getDuration(int index){
        return explain.get(index).duration;
    }

    public String getButtonText(int index){
        return explain.get(index).button;
    }
    public String getButton2Text(int index){
        return explain.get(index).button2;
    }

}
