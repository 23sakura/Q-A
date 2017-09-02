package com.example.haruka.rescue_aid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/1/2017 AD.
 */

public class EmergencyExplanation {



    private AssetManager assetManager;
    private ArrayList<EmergencySituation> explain;
    public boolean isMetronomeRequired;

    public EmergencyExplanation(Context context, String situation){
        assetManager = context.getResources().getAssets();
        explain = new ArrayList();
        try {
            Log.d(EmergencyExplanation.class.getSimpleName(), Integer.toString(assetManager.list(situation).length));
        }catch (Exception e ) {

        }
        try {

            //for (int i = 0; i < assetManager.list(situation).length; i++) {
            for (int i = 0; i < 2; i++) {
                    EmergencySituation es = new EmergencySituation(situation, i, assetManager);
                    explain.add(es);
            }

        } catch (Exception e){

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
}
