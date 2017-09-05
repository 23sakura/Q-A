package com.example.haruka.rescue_aid.utils;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/5/2017 AD.
 */

public class MedicalCertification {
    ArrayList<Record> records;

    public MedicalCertification(){
        records = new ArrayList<>();
        Record r = new Record("start", "");
        records.add(r);
    }

    public void addRecord(Record r){
        records.add(r);
    }

    public void showRecords(){
        for(Record r : records){
            Log.d("records", r.toString());
        }
    }
}
