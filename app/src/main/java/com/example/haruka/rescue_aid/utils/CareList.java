package com.example.haruka.rescue_aid.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by Tomoya on 9/15/2017 AD.
 */

public class CareList {

    private final String _carelist = "care/carelist_v00.csv";
    private AssetManager assetManager;
    public static ArrayList<Care> careList;
    public static int CARE_NUM;

    public CareList(Context context){
        assetManager = context.getResources().getAssets();
        careList = new ArrayList<>();

        try {
            InputStream is = assetManager.open(_carelist);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);

            Log.i("Carelist", "file read success");
            String line = "";
            while ((line = bufferReader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                Log.i("Carelist", "String Tokenizer is made");

                String _index = st.nextToken();
                Log.d("Carelist", _index);
                int index = Integer.parseInt(_index);
                Log.d("Carelist", Integer.toString(index));
                String name = st.nextToken();
                Log.d("Carelist", name);
                String xml = "";
                try {
                    xml = st.nextToken();
                } catch (NoSuchElementException ne){
                    Log.e("Carelist", ne.toString());
                    xml = Care.nullXML;
                }
                Log.d("Carelist", xml);

                Care c = new Care(index, name, xml);
                careList.add(c);
            }

            is.close();
        } catch (Exception e){
            Log.e("Carelist", e.toString());
            careList = new ArrayList<>();
        }

        CARE_NUM = careList.size();
    }

    public static Care getCare(int index){
        return careList.get(index);
    }

    public void showCareList(){
        Log.d("show care list", this.getClass().getSimpleName());
        for(Care c : careList){
            Log.d("care" + Integer.toString(c.index), c.name + ", " + c.xml);
        }
    }
}
