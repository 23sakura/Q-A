package com.example.haruka.rescue_aid.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.haruka.rescue_aid.utils.QADateFormat.getDate;

/**
 * Created by Tomoya on 9/5/2017 AD.
 * This class is the certification of this application.
 * It has information of time, location, interview and cares.
 */

public class MedicalCertification implements Serializable, Comparable<MedicalCertification> {

    private static final long serialVersionUID = Utils.serialVersionUID_MedicalCertification;

    public String FILENAME = "";
    public  String name = "";
    public long number;

    public ArrayList<Record> records;
    //Location location;
    //TODO location should be back to private
    public double[] location;
    private boolean isLocationSet;
    private final int LONGITUDE = 0, LATITUDE = 1;
    private final String LOCATION_TAG = "loc";
    private final String SCENARIO_TAG = "sce";;
    //private final String DEFAULT_ADDRESS = " 徳島県阿南市見能林町青木";
    public final static String DEFAULT_ADDRESS = " ー ";
    Date startAt, endAt;
    int scenarioID;
    //List<Address> addresses;
    //Address address;
    String addressString, addressStringShort;

    public static final int SCENARIO_ID_ILL = 0;
    public static final int SCENARIO_ID_INJURY = 1;

    public MedicalCertification(){
        startAt = new Date();
        endAt = new Date();
        records = new ArrayList<>();
        Record r = new Record("start", "");
        records.add(r);
        location = new double[2];
        isLocationSet = false;

        setFilename();
    }

    public MedicalCertification(String code){
        //TODO test data using QA reader
        records = new ArrayList<>();
        startAt = null;
        endAt = null;
        location = new double[2];
        isLocationSet = false;

        String[] array = code.split("\n");
        for (String line : array){
            Log.d("constructor MC", line);
            Record r;
            if (null == startAt) {
                r = new Record(line);
                startAt = getDate(r.getTime());
                setFilename();
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
                    continue;
                }catch (Exception e){
                    Log.e("records location", e.toString());
                }
            } else if (SCENARIO_TAG.equals(r.getTag())) {
                setScenario(Integer.parseInt(r.getValue()));
                continue;
            } else if ("end".equals(r.getTag())){
                endAt = QADateFormat.getDate(r.getTime());
                continue;
            } else{
            }
            records.add(r);
        }

        Log.d("is location set", Boolean.toString(isLocationSet));
        showLocation();
    }

    private void setFilename() {

        String name = QADateFormat.getStringDateFilename(startAt);
        //name = QADateFormat.getInstanceFilename();
        number = Long.parseLong(name);
        FILENAME = name + ".obj";
        this.name = QADateFormat.getStringDateFilename2(startAt);
    }


    public void addRecord(Record r){
        records.add(r);
        endAt = new Date();
    }

    public void updateRecord(Record r){
        String tag = r.getTag();
        Log.d("RECORD TAG", r.getTagValue());
        //for (Record record : records){
        for(int i = 0; i < records.size(); i++){
            Record record = records.get(i);
            Log.d("RECORD TAG", record.getTagValue());
            if(tag.equals(record.getTag())){
                remove(record);
                break;
            }
        }
        addRecord(r);
    }

    public void remove(Record r) {
        records.remove(r);
    }

    public void updateLocation(Context context) {
        if (addressString == null) {
            try {
                Geocoder coder = new Geocoder(context);
                List<Address> addresses = coder.getFromLocation(this.location[LATITUDE], this.location[LONGITUDE], 1);
                Address address = addresses.get(0);
                Log.d("Geocoder location", address.getLocality());
                setAddressString(getAddressLine(address));
            } catch (Exception e) {
                Log.e("Geocoder location2", e.toString());
            }
        }
    }

    public void updateLocation(Location location, Context context){
        //this.location = location;
        isLocationSet = true;
        this.location[LONGITUDE] = location.getLongitude();
        this.location[LATITUDE] = location.getLatitude();

        if(addressString == null) {
            try {
                Geocoder coder = new Geocoder(context);
                List<Address> addresses = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Address address = addresses.get(0);
                Log.d("Geocoder location", address.getLocality());
                setAddressString(getAddressLine(address));
            } catch (Exception e) {
                Log.e("Geocoder location2", e.toString());
            }
        }
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
        Log.d("records", "is location set " + Boolean.toString(isLocationSet));
    }

    public String toString(){
        showRecords();
        String res = "";
        Date startAt = null;

        for(Record r : records){
            if ("".equals(res)){
                res += r.toString();
                startAt = getDate(r.getTime());
            }else{
                Date d = getDate(r.getTime());
                String time = Long.toString(((d.getTime() - startAt.getTime())/1000));
                res += time + "," + r.getTagValue();
            }
            res += "\n";
        }
        if (isLocationSet){
            Record r = new Record(LOCATION_TAG, Double.toString(location[LONGITUDE]) + ":" + Double.toString(location[LATITUDE]));
            res += "0," + r.getTagValue() + "\n";
        }
        res += Long.toString((endAt.getTime() - startAt.getTime())/1000) + ",end, ";

        Log.d("records", "toString : " + res);
        return res;
    }

    public String getStartAtJap(){
        return QADateFormat.getStringDateJapanese(startAt);
    }

    public String getEndAtJap(){
        return QADateFormat.getStringDateJapanese(endAt);
    }

    public void setAddressString(String addressString){
        this.addressString = addressString;
    }

    public String getAddressLine(Address address) {
        String adr = "";
        adr += address.getAdminArea();
        if (address.getSubAdminArea() != null) {
            adr += address.getSubAdminArea();
        }
        if (address.getLocality() != null) {
            adr += address.getLocality();
        }
        addressStringShort = adr;
        if (address.getSubLocality() != null) {
            adr += address.getSubLocality();
        }
        if (address.getThoroughfare() != null) {
            adr += address.getThoroughfare();
        }
        if (address.getSubThoroughfare() != null) {
            adr += address.getSubThoroughfare();
        }
        return adr;
    }

    public String getAddress(Context context){
        if(addressString == null) {
            try {
                Geocoder coder = new Geocoder(context);
                Log.d("getAddress", Double.toString(location[LATITUDE]) + " " + Double.toString(location[LONGITUDE]));
                List<Address> addresses = coder.getFromLocation(location[LATITUDE], location[LONGITUDE], 1);
                Address address = addresses.get(0);
                setAddressString(getAddressLine(address));
                Log.d("Geocoder location", addresses.get(0).getLocality());
                return getAddressLine(address);
            } catch (Exception e) {
                Log.e("Geocoder location1", e.toString());
                return DEFAULT_ADDRESS;
            }
        } else{
            return addressString;
        }
    }

    public String getAddress(){
        if (addressString != null){
            return addressString;
        } else{
            return DEFAULT_ADDRESS;
        }
    }

    public void setScenario(int scenarioID){
        this.scenarioID = scenarioID;
        addRecord(new Record(SCENARIO_TAG, Integer.toString(scenarioID)));
        if (scenarioID == SCENARIO_ID_ILL){
            name += " 急病";
        } else {
            name += " ケガ";
        }
    }

    public String getCallNote(ArrayList<Question> questions){
        String res = "";

        for(Record record : records){
            int index;
            try{
                index = Integer.parseInt(record.getTag());
            } catch (NumberFormatException e){
                continue;
            }
            Question q = questions.get(index);
            res += q.getQuestion() + "：" + Utils.getAnswerString(record.getValue()) + "\n";
        }

        if (addressString != null){
            res +=  "\n" + addressString + "\n";
            res +=  "　　東経：" + Utils.getDMSLocation(location[LONGITUDE]) + "\n";
            res +=  "　　北緯：" + Utils.getDMSLocation(location[LATITUDE]);
        }

        return res;
    }

    public String getCallNoteAddress(){
        String res = "";
        if (addressString != null){
            res +=  "\n" + addressString + "\n";
            res +=  "　　東経：" + Utils.getDMSLocation(location[LONGITUDE]) + "\n";
            res +=  "　　北緯：" + Utils.getDMSLocation(location[LATITUDE]);
        }

        return res;
    }

    public String getCallNoteAddressShort(){
        String res = "";
        if (addressStringShort != null){
            res +=  "\n" + addressStringShort;
        }
        return res;
    }

    public int getScenarioID(){
        return scenarioID;
    }

    public void save(Context context){
        TempDataUtil.store(context, this);
    }

    public static boolean[] makeCareList(String care_){
        String[] careNums = care_.split(":");
        boolean[] careList = new boolean[Utils.NUM_CARE];
        for(String c : careNums){
            try {
                int index = Integer.parseInt(c.trim());
                careList[index] = true;
            } catch (Exception e){
                Log.e("make care list", e.toString());
            }
        }
        return careList;
    }

    public JSONObject getJSON(){
        int additional = 0;
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject();

            for (int i = 0; i < records.size(); i++){
                Record record = records.get(i);
                String name = "record" + Integer.toString(i);

                String time;
                if (record.getTag().equals(Utils.TAG_CARE)){
                    time = Long.toString(QADateFormat.getDate(record.getTime()).getTime() - startAt.getTime());
                } else {
                    time = record.getTime();
                }
                JSONObject recordJSON = new JSONObject();
                recordJSON.put("time", time);
                recordJSON.put("tag", record.getTag());
                recordJSON.put("val", record.getValue());

                jsonObject.put(name, recordJSON);
            }
            if (isLocationSet){
                additional += 1;
                String name = "record" + Integer.toString(records.size());
                JSONObject recordJSON = new JSONObject();
                recordJSON.put("time", records.get(0).getTime());
                recordJSON.put("tag", "loc");
                recordJSON.put("val", Double.toString(location[LATITUDE]) + ":" + Double.toString(location[LONGITUDE]));

                jsonObject.put(name, recordJSON);
            }
            String name = "end";
            JSONObject recordJSON = new JSONObject();
            recordJSON.put("time", QADateFormat.getStringDate(endAt));
            recordJSON.put("tag", "end");
            recordJSON.put("val", "");
            jsonObject.put(name, recordJSON);


            jsonObject.put("len", Integer.toString(records.size()+additional));
            Log.d("jsonobject", jsonObject.toString());

        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public int compareTo(MedicalCertification m){
        return (int)(m.number - this.number);
    }
}
