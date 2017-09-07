package com.example.haruka.rescue_aid.utils;

import android.location.Location;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Tomoya on 9/5/2017 AD.
 */

public class MedicalCertification implements Serializable {

    private static final long serialVersionUID = 1L;

    ArrayList<Record> records;
    /*
    String[] times;
    String[] tags;
    String[] values;
    */

    Location location;


    public MedicalCertification(){
        records = new ArrayList<>();
        Record r = new Record("start", "");
        records.add(r);

        /*
        times = new String[1];
        times[0] = r.time;
        tags = new String[1];
        tags[0] = r.tag;
        values = new String[];
        */
    }



    public void addRecord(Record r){
        records.add(r);
    }

    public void updateLocation(Location location){
        this.location = location;
    }

    public void showRecords(){
        for(Record r : records){
            Log.d("records", r.toString());
        }
        try {
            Log.d("records", "location : " + Double.toString(location.getLongitude()) + ", " + Double.toString(location.getLongitude()));
        }catch(NullPointerException e){
            Log.e(MedicalCertification.class.getSimpleName(), e.toString());
        }
    }

    public String toString(){
        showRecords();
        String res = "";
        Date startAt = null;

        for(Record r : records){
            if ("".equals(res)){
                res += r.toString();
                startAt = QADateFormat.getDate(r.getTime());
            }else{
                Date d = QADateFormat.getDate(r.getTime());
                String time = Long.toString(((d.getTime() - startAt.getTime())/1000));
                res += time + "," + r.getTagValue();
            }
            res += "\n";
        }

        return res;
    }

}
