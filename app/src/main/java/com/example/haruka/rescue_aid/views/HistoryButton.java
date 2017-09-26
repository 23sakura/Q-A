package com.example.haruka.rescue_aid.views;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Question;
import com.example.haruka.rescue_aid.utils.Utils;

/**
 * Created by Tomoya on 9/14/2017 AD.
 * This Button is used in InterviewActivity to show the history of interview
 */

public class HistoryButton extends Button {

    public int index;
    public int urgency;
    public boolean[] cares;

    public HistoryButton(Context context, int index) {
        super(context);
        this.index = index;
        this.urgency = Utils.MIN_URGNECY;
        setHeight(180);
    }

    public void setText(Question q){
        if(q.isUnsure) {
            setTextColor(getResources().getColor(R.color.unsure));
            setText(q.getQuestion() + "\nわからない");
        } else {
            if (q.getAnswer()) {
                setTextColor(getResources().getColor(R.color.yes));
            } else {
                setTextColor(getResources().getColor(R.color.no));
            }
            setText(q.getQuestion() + "\n" + q.getAnswerString());
        }
        this.urgency = q.getUrgency();
        this.cares = q.getCares();
        Log.d("HistoryButton", getCareString());
    }


    public String getCareString(){
        String s = "";
        for (boolean c : cares){
            s += c ? "Y" : "N";
        }

        return s;
    }
}

