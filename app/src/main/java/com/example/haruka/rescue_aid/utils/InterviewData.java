package com.example.haruka.rescue_aid.utils;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tomoya on 8/29/2017 AD.
 */

public class InterviewData {

    private String dateBeginning;
    private ArrayList<Question> questions;
    private Location location;

    public InterviewData(Location location){
        questions = new ArrayList<>();
        this.location = location;
        dateBeginning = QADateFormat.getInstance();
    }

    public void setListOfQuestions(ArrayList<Question> questions){
        this.questions = questions;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public void updateLocation(Location location){
        this.location = location;
    }

    public Location getLocation(){
        return location;
    }

    public String toString(){
        JSONObject interviewJSON = new JSONObject();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("location", location.toString());
            jsonObject.put("begin@", dateBeginning);
            interviewJSON.put("Begin@", jsonObject);

            JSONArray jsonArray = new JSONArray();
            for(Question q : questions) {
                jsonObject = new JSONObject();
                jsonObject.put(Integer.toString(q.getIndex()), q.getAnswer());
                jsonArray.put(jsonObject);
            }
            interviewJSON.put("Question", jsonArray);


        } catch(Exception e) {

        }
        return interviewJSON.toString();
    }
}
