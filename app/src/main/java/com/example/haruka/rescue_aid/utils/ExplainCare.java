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

public class ExplainCare {

    public String title;
    private AssetManager assetManager;
    private ArrayList<EmergencySituation> explain;
    public boolean isMetronomeRequired;

    public int id_;
    public int numSituation;

    public String sub;
    public boolean isActive;

    public ExplainCare(Context context, String situation){
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

            case "care_bleed_stopping":
                xmlID = R.xml.care_bleed_stopping;
                title = "care_bleed_stopping";
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

        boolean isNotice = false;

        explain = new ArrayList();
        try{
            int eventType = xpp.getEventType();
            while(eventType != XmlResourceParser.END_DOCUMENT){
                final String name = xpp.getName();
                Log.d("tag loop", " " +name);
                if (name == null){
                    Log.d("xpp", "name is null");
                    eventType = xpp.next();
                    continue;
                }
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("tag", "Start Document");
                        break;

                    case XmlPullParser.START_TAG:
                        if ("id".equals(name)) {
                            //Log.d("tag", "id");
                            id_ = Integer.parseInt(xpp.nextText());
                            Log.d("tag id", Integer.toString(id_));
                        } else if ("num".equals(name)){
                            numSituation = Integer.parseInt(xpp.nextText());
                        } else if ("items".equals(name)) {
                            Log.d("tag", "items");
                            //explain = new ArrayList();
                        } else if ("item".equals(name)) {
                            Log.d("tag", "item");
                            emergencySituation = new EmergencySituation();
                        }  else if ("notice".equals(name)) {
                            Log.d("tag", "notice");
                            isNotice = true;
                        } else if ("description".equals(name)) {
                            String s = xpp.nextText();
                            Log.d("tag description", s);
                            if (isNotice){
                                //TODO save as notice description
                            }else {
                                emergencySituation.text = s;
                            }
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
                            //explain.add(emergencySituation);
                        } else if ("notice".equals(name)) {
                            Log.d("tag", "notice");
                            isNotice = false;
                        }
                        break;
                    default:
                        break;
                }

                eventType = xpp.next();
            }
            Log.d("tag end document", Boolean.toString(eventType == XmlResourceParser.END_DOCUMENT));

        }catch (Exception e){
            Log.e("Emergency", e.toString());
        }
    }

    public String getText(int index){
        return explain.get(index).text;
    }

    public Drawable getImage(int index){
        return explain.get(index).drawable;
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
