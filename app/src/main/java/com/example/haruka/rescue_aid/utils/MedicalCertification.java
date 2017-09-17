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
    //Location location;
    private double[] location;
    private boolean isLocationSet;
    private final int LONGITUDE = 0, LATITUDE = 1;
    private final String LOCATION_TAG = "loc";

    public MedicalCertification(){
        records = new ArrayList<>();
        Record r = new Record("start", "");
        records.add(r);
        location = new double[2];
        isLocationSet = false;
        /*
        times = new String[1];
        times[0] = r.time;
        tags = new String[1];
        tags[0] = r.tag;
        values = new String[];
        */
    }

    public MedicalCertification(String code){
        //TODO test data using QA reader
        records = new ArrayList<>();
        Date startAt = null;
        location = new double[2];
        isLocationSet = false;

        String[] array = code.split("\n");
        for (String line : array){
            Log.d("constructor MC", line);
            Record r;
            if (null == startAt){
                r = new Record(line);
                startAt = QADateFormat.getDate(r.getTime());
            } else {
                r = new Record(startAt, line);
            }
            if (LOCATION_TAG.equals(r.getTag())){
                try {
                    String[] loc = r.getValue().split(":");
                    location = new double[2];
                    location[LONGITUDE] = Double.parseDouble(loc[LONGITUDE]);
                    location[LATITUDE] = Double.parseDouble(loc[LATITUDE]);
                    isLocationSet = true;
                }catch (Exception e){
                    Log.e("records location", e.toString());
                }
            } else{
                records.add(r);
            }
        }

        showLocation();

    }

    public void addRecord(Record r){
        records.add(r);
    }
    public void remove(Record r) {}

    public void updateLocation(Location location){
        //this.location = location;
        isLocationSet = true;
        this.location[LONGITUDE] = location.getLongitude();
        this.location[LATITUDE] = location.getLatitude();
    }

    public Location getLocation(){
        Location l = new Location("");
        if (isLocationSet) {
            l.setLongitude(location[LONGITUDE]);
            l.setLatitude(location[LATITUDE]);
        }
        return l;
    }

    public void showLocation(){
        if (!isLocationSet){
            Log.i("medical certification", "location is not set");
        } else {
            Log.i("medical certification", "location : "+ location[LONGITUDE] + ", " + location[LATITUDE]);
        }
    }

    public void showRecords(){
        showRecords("");
    }

    public void showRecords(String activity){
        Log.d("records", activity);
        for(Record r : records){
            Log.d("records", r.toString());
        }
        if (isLocationSet){
            Log.d("records", "location : " + Double.toString(location[LONGITUDE]) + ", " + Double.toString(location[LATITUDE]));
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
        if (isLocationSet){
            Record r = new Record(LOCATION_TAG, Double.toString(location[LONGITUDE]) + ":" + Double.toString(location[LATITUDE]));
            res += "0," + r.getTagValue();
        }

        return res;
    }

}
