package com.example.haruka.rescue_aid.utils;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tomoya on 9/2/2017 AD.
 */


public class EmergencySituation{

    String situation;
    int id;

    public String text;
    public String button;
    public Drawable drawable;
    public int duration;

    public EmergencySituation(){
        this.situation = situation;
        id = 0;
        button = "";
    }

    public EmergencySituation(String situation){
        this.situation = situation;
        id = 0;
    }

    EmergencySituation(String situation, int id){
        this.situation = situation;
        this.id = id;
    }

    EmergencySituation(String situation, int id, AssetManager assetManager){
        this(situation, id);
        setAssetManager(assetManager);
    }

    EmergencySituation(String description, Drawable drawable){
        situation = "";
        id = 0;
        this.text = description;
        this.drawable = drawable;
    }

    public void setAssetManager(AssetManager assetManager){
        try {
            InputStream is = assetManager.open("explain/" + situation + "/i" + String.format("%03d", id) + "/text.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);

            text = "";
            String line = bufferReader.readLine();
            while(line != null){
                text += line + "\n";
                line = bufferReader.readLine();
            }

            is = assetManager.open("explain/" + situation + "/i" + String.format("%03d", id) + "/img.png");
            drawable = Drawable.createFromStream(is, null);
        }catch (Exception e){
            Log.e("EmergencyExplanation-2", e.toString());
        }
    }

    public String getText(){
        return text;
    }

    public Drawable getImage(){
        return drawable;
    }
}