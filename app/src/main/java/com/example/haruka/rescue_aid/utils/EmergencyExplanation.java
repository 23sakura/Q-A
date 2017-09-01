package com.example.haruka.rescue_aid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tomoya on 9/1/2017 AD.
 */

public class EmergencyExplanation {

    private AssetManager assetManager;
    private String text;
    private Drawable drawable;

    public EmergencyExplanation(Context context, String situation){
        assetManager = context.getResources().getAssets();
        try {
            InputStream is = assetManager.open("explain/texts/test" + ".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            text = bufferReader.readLine();
            Log.d("EmergencyExplanation", text);

            is = assetManager.open("explain/images/chest_compression" + ".png");
            drawable = Drawable.createFromStream(is, null);
        }catch (Exception e){
            Log.e("EmergencyExplanation", e.toString());
        }
    }

    public String getText(){
        return text;
    }

    public Drawable getImage(){
        return drawable;
    }
}
